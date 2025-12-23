package com.MedXpress.dto.pharmacyMedicine;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PharmacyMedicineUpsertRequest {

    @NotNull
    private UUID pharmacyId;

    @NotNull
    private UUID medicineId;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @Min(0)
    private int stockQuantity;

    private Boolean isAvailable;
}
