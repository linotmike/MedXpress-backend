package com.MedXpress.mapper;

import com.MedXpress.dto.order.OrderItemResponse;
import com.MedXpress.dto.order.OrderResponse;
import com.MedXpress.entity.Medicine;
import com.MedXpress.entity.Order;
import com.MedXpress.entity.OrderItem;
import com.MedXpress.entity.PharmacyMedicine;

import java.util.List;

public class OrderMapper {

    private OrderMapper() {}

    public static OrderItemResponse toItemResponse(OrderItem item) {
        PharmacyMedicine pm = item.getPharmacyMedicine();
        Medicine m = pm.getMedicine();

        return OrderItemResponse.builder()
                .id(item.getId())
                .pharmacyMedicineId(pm.getId())
                .medicineId(m.getId())
                .medicineName(m.getName())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .lineTotal(item.getLineTotal())
                .build();
    }

    public static OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderMapper::toItemResponse)
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .patientId(order.getPatient().getId())
                .pharmacyId(order.getPharmacy().getId())
                .addressId(order.getAddress().getId())
                .paymentMethod(order.getPaymentMethod().name())
                .status(order.getStatus().name())
                .deliveryFee(order.getDeliveryFee())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }
}
