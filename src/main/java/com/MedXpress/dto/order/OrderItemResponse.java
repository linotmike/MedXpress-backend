package com.MedXpress.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemResponse {

    private UUID id;

    private UUID pharmacyMedicineId;
    private UUID medicineId;
    private String medicineName;

    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
