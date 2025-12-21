package com.MedXpress.mapper;

import com.MedXpress.dto.medicine.MedicineCreateRequest;
import com.MedXpress.dto.medicine.MedicineResponse;
import com.MedXpress.entity.Medicine;

public class MedicineMapper {

    private MedicineMapper() {}

    public static Medicine toEntity(MedicineCreateRequest req) {
        return Medicine.builder()
                .name(req.getName())
                .brandName(req.getBrandName())
                .form(req.getForm())
                .strength(req.getStrength())
                .description(req.getDescription())
                .build();
    }

    public static MedicineResponse toResponse(Medicine m) {
        return MedicineResponse.builder()
                .id(m.getId())
                .name(m.getName())
                .brandName(m.getBrandName())
                .form(m.getForm())
                .strength(m.getStrength())
                .description(m.getDescription())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .build();
    }
}
