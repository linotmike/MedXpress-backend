package com.MedXpress.dto.delivery;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeliveryStatusUpdateRequest {

    @NotBlank
    private String status; // PICKED_UP, ON_THE_WAY, DELIVERED, FAILED

    private String failureReason; // required when FAILED
}
