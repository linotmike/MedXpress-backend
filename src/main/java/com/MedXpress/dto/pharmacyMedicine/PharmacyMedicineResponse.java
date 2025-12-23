package com.MedXpress.dto.pharmacyMedicine;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class PharmacyMedicineResponse {

    private UUID id;

    private UUID pharmacyId;
    private UUID medicineId;

    private String medicineName;
    private String brandName;
    private String form;
    private String strength;

    private BigDecimal price;
    private int stockQuantity;
    private boolean isAvailable;

    private OffsetDateTime lastUpdatedAt;
}
