package com.MedXpress.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class DeliveryAssignRequest {

    @NotNull
    private UUID orderId;

    @NotNull
    private UUID riderId;
}
