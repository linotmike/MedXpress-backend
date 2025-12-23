package com.MedXpress.dto.pharmacy;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class PharmacyResponse {

    private UUID id;
    private String name;
    private String licenseNumber;
    private String phoneNumber;
    private String email;
    private String addressLine;
    private Double latitude;
    private Double longitude;

    private UUID ownerUserId;
    private String status;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
