package com.MedXpress.mapper;

import com.MedXpress.dto.address.AddressCreateRequest;
import com.MedXpress.dto.address.AddressResponse;
import com.MedXpress.entity.Address;
import com.MedXpress.entity.User;

public class AddressMapper {

    private AddressMapper() {}

    public static Address toEntity(AddressCreateRequest req, User user) {
        return Address.builder()
                .user(user)
                .label(req.getLabel())
                .city(req.getCity())
                .subCity(req.getSubCity())
                .woreda(req.getWoreda())
                .houseNumber(req.getHouseNumber())
                .directions(req.getDirections())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .isDefault(Boolean.TRUE.equals(req.getIsDefault()))
                .build();
    }

    public static AddressResponse toResponse(Address a) {
        return AddressResponse.builder()
                .id(a.getId())
                .userId(a.getUser().getId())
                .label(a.getLabel())
                .city(a.getCity())
                .subCity(a.getSubCity())
                .woreda(a.getWoreda())
                .houseNumber(a.getHouseNumber())
                .directions(a.getDirections())
                .latitude(a.getLatitude())
                .longitude(a.getLongitude())
                .isDefault(a.isDefault())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}
