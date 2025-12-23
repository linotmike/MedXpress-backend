package com.MedXpress.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AddressCreateRequest {

    @NotNull
    private UUID userId;

    private String label;

    @NotBlank
    private String city;

    private String subCity;
    private String woreda;
    private String houseNumber;
    private String directions;

    private Double latitude;
    private Double longitude;

    private Boolean isDefault;
}
