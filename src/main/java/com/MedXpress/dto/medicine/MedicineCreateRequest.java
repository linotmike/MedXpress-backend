package com.MedXpress.dto.medicine;

import com.MedXpress.util.MedicineForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MedicineCreateRequest {

    @NotBlank
    private String name;

    private String brandName;

    @NotNull
    private MedicineForm form;

    private String strength;

    private String description;
}
