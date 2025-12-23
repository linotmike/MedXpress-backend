package com.MedXpress.mapper;

import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineResponse;
import com.MedXpress.entity.Medicine;
import com.MedXpress.entity.PharmacyMedicine;

public class PharmacyMedicineMapper {

    private PharmacyMedicineMapper() {}

    public static PharmacyMedicineResponse toResponse(PharmacyMedicine pm) {
        Medicine m = pm.getMedicine();

        return PharmacyMedicineResponse.builder()
                .id(pm.getId())
                .pharmacyId(pm.getPharmacy().getId())
                .medicineId(m.getId())
                .medicineName(m.getName())
                .brandName(m.getBrandName())
                .form(m.getForm().name())
                .strength(m.getStrength())
                .price(pm.getPrice())
                .stockQuantity(pm.getStockQuantity())
                .isAvailable(pm.isAvailable())
                .lastUpdatedAt(pm.getLastUpdatedAt())
                .build();
    }
}
