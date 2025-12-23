package com.MedXpress.service.order.impl;

import com.MedXpress.dto.order.OrderCreateRequest;
import com.MedXpress.dto.order.OrderResponse;
import com.MedXpress.entity.*;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.OrderMapper;
import com.MedXpress.repository.*;
import com.MedXpress.service.order.OrderService;
import com.MedXpress.util.OrderStatus;
import com.MedXpress.util.PaymentMethod;
import com.MedXpress.util.PharmacyStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final AddressRepository addressRepository;
    private final PharmacyMedicineRepository pharmacyMedicineRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            UserRepository userRepository,
            PharmacyRepository pharmacyRepository,
            AddressRepository addressRepository,
            PharmacyMedicineRepository pharmacyMedicineRepository
    ) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.addressRepository = addressRepository;
        this.pharmacyMedicineRepository = pharmacyMedicineRepository;
    }

    @Override
    public OrderResponse create(OrderCreateRequest request) {

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException("Order must contain at least one item.");
        }

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new NotFoundException("Patient not found."));

        Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        if (pharmacy.getStatus() != PharmacyStatus.APPROVED) {
            throw new BusinessException("Pharmacy must be APPROVED to accept orders.");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new NotFoundException("Address not found."));

        if (!address.getUser().getId().equals(patient.getId())) {
            throw new BusinessException("Address does not belong to this patient.");
        }

        BigDecimal deliveryFee = request.getDeliveryFee() == null
                ? BigDecimal.ZERO
                : request.getDeliveryFee();

        if (deliveryFee.signum() < 0) {
            throw new BusinessException("Delivery fee must be >= 0.");
        }

        PaymentMethod paymentMethod = request.getPaymentMethod() == null
                ? PaymentMethod.CASH_ON_DELIVERY
                : request.getPaymentMethod();

        Order order = Order.builder()
                .patient(patient)
                .pharmacy(pharmacy)
                .address(address)
                .paymentMethod(paymentMethod)
                .status(OrderStatus.PENDING_PHARMACY_CONFIRMATION)
                .deliveryFee(deliveryFee)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal itemsTotal = BigDecimal.ZERO;

        for (OrderCreateRequest.OrderItemCreateRequest itemReq : request.getItems()) {
            if (itemReq.getQuantity() <= 0) {
                throw new BusinessException("Item quantity must be >= 1.");
            }

            PharmacyMedicine pm = pharmacyMedicineRepository.findById(itemReq.getPharmacyMedicineId())
                    .orElseThrow(() -> new NotFoundException("Pharmacy medicine item not found."));

            // Ensure the item belongs to the selected pharmacy
            if (!pm.getPharmacy().getId().equals(pharmacy.getId())) {
                throw new BusinessException("Item does not belong to the selected pharmacy.");
            }

            if (!pm.isAvailable()) {
                throw new BusinessException("Item is not available: " + pm.getMedicine().getName());
            }

            if (pm.getStockQuantity() < itemReq.getQuantity()) {
                throw new BusinessException("Insufficient stock for: " + pm.getMedicine().getName());
            }

            BigDecimal unitPrice = pm.getPrice();
            BigDecimal lineTotal = unitPrice
                    .multiply(BigDecimal.valueOf(itemReq.getQuantity()))
                    .setScale(2, RoundingMode.HALF_UP);

            OrderItem oi = OrderItem.builder()
                    .order(order)
                    .pharmacyMedicine(pm)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)
                    .lineTotal(lineTotal)
                    .build();

            order.getItems().add(oi);

            // Decrement stock (transactional)
            pm.setStockQuantity(pm.getStockQuantity() - itemReq.getQuantity());
            if (pm.getStockQuantity() <= 0) {
                pm.setStockQuantity(0);
                pm.setAvailable(false);
            }
            pharmacyMedicineRepository.save(pm);

            itemsTotal = itemsTotal.add(lineTotal);
        }

        BigDecimal finalTotal = itemsTotal.add(deliveryFee).setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(finalTotal);

        Order saved = orderRepository.save(order);
        return OrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(UUID orderId) {
        Order o = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found."));
        return OrderMapper.toResponse(o);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<OrderResponse> listByPatient(UUID patientId) {
        return orderRepository.findByPatient_Id(patientId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<OrderResponse> listByPharmacy(UUID pharmacyId) {
        return orderRepository.findByPharmacy_Id(pharmacyId)
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}
