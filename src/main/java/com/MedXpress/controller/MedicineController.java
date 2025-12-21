package com.MedXpress.controller;

import com.MedXpress.dto.medicine.MedicineCreateRequest;
import com.MedXpress.dto.medicine.MedicineResponse;
import com.MedXpress.service.medicine.MedicineService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medicines")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicineResponse create(@Valid @RequestBody MedicineCreateRequest request) {
        return medicineService.create(request);
    }

    @GetMapping("/{medicineId}")
    public MedicineResponse getById(@PathVariable UUID medicineId) {
        return medicineService.getById(medicineId);
    }

    @GetMapping
    public List<MedicineResponse> list(@RequestParam(required = false) String q) {
        return (q == null || q.isBlank())
                ? medicineService.listAll()
                : medicineService.searchByName(q);
    }
}
