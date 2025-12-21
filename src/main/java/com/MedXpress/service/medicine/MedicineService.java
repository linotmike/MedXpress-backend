package com.MedXpress.service.medicine;

import com.MedXpress.dto.medicine.MedicineCreateRequest;
import com.MedXpress.dto.medicine.MedicineResponse;

import java.util.List;
import java.util.UUID;

public interface MedicineService {

    MedicineResponse create(MedicineCreateRequest request);

    MedicineResponse getById(UUID medicineId);

    List<MedicineResponse> searchByName(String name);

    List<MedicineResponse> listAll();
}
