package com.MedXpress.mapper;

import com.MedXpress.dto.pharmacy.PharmacyCreateRequest;
import com.MedXpress.dto.pharmacy.PharmacyResponse;
import com.MedXpress.entity.Pharmacy;
import com.MedXpress.entity.User;

public class PharmacyMapper {

    private PharmacyMapper() {}

    public static Pharmacy toEntity(PharmacyCreateRequest req, User owner) {
        return Pharmacy.builder()
                .name(req.getName())
                .licenseNumber(req.getLicenseNumber())
                .phoneNumber(req.getPhoneNumber())
                .email(req.getEmail())
                .addressLine(req.getAddressLine())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .ownerUser(owner)
                .build();
    }

    public static PharmacyResponse toResponse(Pharmacy p) {
        return PharmacyResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .licenseNumber(p.getLicenseNumber())
                .phoneNumber(p.getPhoneNumber())
                .email(p.getEmail())
                .addressLine(p.getAddressLine())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .ownerUserId(p.getOwnerUser().getId())
                .status(p.getStatus().name())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}
