package com.MedXpress.service.pharmacyMedicine;

import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineResponse;
import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineUpsertRequest;

import java.util.List;
import java.util.UUID;

public interface PharmacyMedicineService {

    PharmacyMedicineResponse upsert(PharmacyMedicineUpsertRequest request);

    PharmacyMedicineResponse getById(UUID pharmacyMedicineId);

    List<PharmacyMedicineResponse> listByPharmacy(UUID pharmacyId);

    List<PharmacyMedicineResponse> listByMedicine(UUID medicineId);
}
