package com.MedXpress.dto.delivery;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class DeliveryResponse {

    private UUID id;

    private UUID orderId;
    private UUID riderId;

    private String status;

    private OffsetDateTime assignedAt;
    private OffsetDateTime pickedUpAt;
    private OffsetDateTime deliveredAt;

    private String failureReason;
}
