package com.MedXpress.mapper;

import com.MedXpress.dto.delivery.DeliveryResponse;
import com.MedXpress.entity.Delivery;

public class DeliveryMapper {

    private DeliveryMapper() {}

    public static DeliveryResponse toResponse(Delivery d) {
        return DeliveryResponse.builder()
                .id(d.getId())
                .orderId(d.getOrder().getId())
                .riderId(d.getRider().getId())
                .status(d.getStatus().name())
                .assignedAt(d.getAssignedAt())
                .pickedUpAt(d.getPickedUpAt())
                .deliveredAt(d.getDeliveredAt())
                .failureReason(d.getFailureReason())
                .build();
    }
}
