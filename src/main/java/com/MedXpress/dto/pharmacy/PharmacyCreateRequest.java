package com.MedXpress.dto.pharmacy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class PharmacyCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String licenseNumber;

    @NotBlank
    private String phoneNumber;

    private String email;

    @NotBlank
    private String addressLine;

    private Double latitude;
    private Double longitude;

    @NotNull
    private UUID ownerUserId;
}
