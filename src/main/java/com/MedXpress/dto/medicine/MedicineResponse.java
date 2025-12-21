package com.MedXpress.dto.medicine;

import com.MedXpress.util.MedicineForm;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class MedicineResponse {

    private UUID id;
    private String name;
    private String brandName;
    private MedicineForm form;
    private String strength;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
