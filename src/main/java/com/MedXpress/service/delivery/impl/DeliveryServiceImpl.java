package com.MedXpress.service.delivery.impl;

import com.MedXpress.dto.delivery.DeliveryAssignRequest;
import com.MedXpress.dto.delivery.DeliveryResponse;
import com.MedXpress.dto.delivery.DeliveryStatusUpdateRequest;
import com.MedXpress.entity.Delivery;
import com.MedXpress.entity.Order;
import com.MedXpress.entity.User;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.DeliveryMapper;
import com.MedXpress.repository.DeliveryRepository;
import com.MedXpress.repository.OrderRepository;
import com.MedXpress.repository.UserRepository;
import com.MedXpress.service.delivery.DeliveryService;
import com.MedXpress.util.DeliveryStatus;
import com.MedXpress.util.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public DeliveryServiceImpl(
            DeliveryRepository deliveryRepository,
            OrderRepository orderRepository,
            UserRepository userRepository
    ) {
        this.deliveryRepository = deliveryRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DeliveryResponse assign(DeliveryAssignRequest request) {

        // One delivery per order
        if (deliveryRepository.findByOrder_Id(request.getOrderId()).isPresent()) {
            throw new BusinessException("Delivery already exists for this order.");
        }

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found."));

        if (order.getStatus() == OrderStatus.CANCELLED || order.getStatus() == OrderStatus.REJECTED) {
            throw new BusinessException("Cannot assign delivery for cancelled/rejected order.");
        }

        User rider = userRepository.findById(request.getRiderId())
                .orElseThrow(() -> new NotFoundException("Rider not found."));

        //  enforce rider role = RIDER (after auth is done)

        Delivery delivery = Delivery.builder()
                .order(order)
                .rider(rider)
                .status(DeliveryStatus.ASSIGNED)
                .build();

        // Update order status
        order.setStatus(OrderStatus.ASSIGNED_TO_RIDER);

        Delivery saved = deliveryRepository.save(delivery);
        return DeliveryMapper.toResponse(saved);
    }

    @Override
    public DeliveryResponse updateStatus(UUID deliveryId, DeliveryStatusUpdateRequest request) {

        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new NotFoundException("Delivery not found."));

        DeliveryStatus next;
        try {
            next = DeliveryStatus.valueOf(request.getStatus());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid delivery status.");
        }

        DeliveryStatus current = delivery.getStatus();

        // strict transitions
        if (current == DeliveryStatus.DELIVERED || current == DeliveryStatus.FAILED) {
            throw new BusinessException("Cannot update a completed delivery.");
        }

        if (next == DeliveryStatus.PICKED_UP && current != DeliveryStatus.ASSIGNED) {
            throw new BusinessException("Delivery must be ASSIGNED before PICKED_UP.");
        }

        if (next == DeliveryStatus.ON_THE_WAY && current != DeliveryStatus.PICKED_UP) {
            throw new BusinessException("Delivery must be PICKED_UP before ON_THE_WAY.");
        }

        if (next == DeliveryStatus.DELIVERED && current != DeliveryStatus.ON_THE_WAY) {
            throw new BusinessException("Delivery must be ON_THE_WAY before DELIVERED.");
        }

        if (next == DeliveryStatus.FAILED) {
            if (request.getFailureReason() == null || request.getFailureReason().isBlank()) {
                throw new BusinessException("failureReason is required when status is FAILED.");
            }
            delivery.setFailureReason(request.getFailureReason());
        }

        delivery.setStatus(next);

        Order order = delivery.getOrder();

        if (next == DeliveryStatus.PICKED_UP) {
            delivery.setPickedUpAt(OffsetDateTime.now());
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        } else if (next == DeliveryStatus.ON_THE_WAY) {
            order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        } else if (next == DeliveryStatus.DELIVERED) {
            delivery.setDeliveredAt(OffsetDateTime.now());
            order.setStatus(OrderStatus.DELIVERED);
        } else if (next == DeliveryStatus.FAILED) {
            order.setStatus(OrderStatus.CANCELLED);
        }

        return DeliveryMapper.toResponse(deliveryRepository.save(delivery));
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getById(UUID deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .map(DeliveryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Delivery not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public DeliveryResponse getByOrderId(UUID orderId) {
        return deliveryRepository.findByOrder_Id(orderId)
                .map(DeliveryMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Delivery not found for this order."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryResponse> listByRider(UUID riderId) {
        return deliveryRepository.findByRider_Id(riderId)
                .stream()
                .map(DeliveryMapper::toResponse)
                .toList();
    }
}
