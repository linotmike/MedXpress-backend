package com.MedXpress.controller;

import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineResponse;
import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineUpsertRequest;
import com.MedXpress.service.pharmacyMedicine.PharmacyMedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacy-medicines")
public class PharmacyMedicineController {

    private final PharmacyMedicineService pharmacyMedicineService;

    public PharmacyMedicineController(PharmacyMedicineService pharmacyMedicineService) {
        this.pharmacyMedicineService = pharmacyMedicineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PharmacyMedicineResponse upsert(
            @Valid @RequestBody PharmacyMedicineUpsertRequest request
    ) {
        return pharmacyMedicineService.upsert(request);
    }

    @GetMapping("/{id}")
    public PharmacyMedicineResponse getById(@PathVariable UUID id) {
        return pharmacyMedicineService.getById(id);
    }

    @GetMapping("/pharmacy/{pharmacyId}")
    public List<PharmacyMedicineResponse> listByPharmacy(@PathVariable UUID pharmacyId) {
        return pharmacyMedicineService.listByPharmacy(pharmacyId);
    }

    @GetMapping("/medicine/{medicineId}")
    public List<PharmacyMedicineResponse> listByMedicine(@PathVariable UUID medicineId) {
        return pharmacyMedicineService.listByMedicine(medicineId);
    }
}
