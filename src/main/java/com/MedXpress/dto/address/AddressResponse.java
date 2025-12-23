package com.MedXpress.dto.address;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class AddressResponse {

    private UUID id;
    private UUID userId;

    private String label;
    private String city;
    private String subCity;
    private String woreda;
    private String houseNumber;
    private String directions;

    private Double latitude;
    private Double longitude;

    private boolean isDefault;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
