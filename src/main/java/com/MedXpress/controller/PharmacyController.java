package com.MedXpress.controller;

import com.MedXpress.dto.pharmacy.PharmacyCreateRequest;
import com.MedXpress.dto.pharmacy.PharmacyResponse;
import com.MedXpress.service.pharmacy.PharmacyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    public PharmacyController(PharmacyService pharmacyService) {
        this.pharmacyService = pharmacyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PharmacyResponse create(@Valid @RequestBody PharmacyCreateRequest request) {
        return pharmacyService.create(request);
    }

    @GetMapping("/{pharmacyId}")
    public PharmacyResponse getById(@PathVariable UUID pharmacyId) {
        return pharmacyService.getById(pharmacyId);
    }

    @GetMapping
    public List<PharmacyResponse> list(
            @RequestParam(required = false) UUID ownerUserId,
            @RequestParam(required = false) String status
    ) {
        if (ownerUserId != null) {
            return pharmacyService.listByOwner(ownerUserId);
        }
        if (status != null && !status.isBlank()) {
            return pharmacyService.listByStatus(status);
        }
        return pharmacyService.listAll();
    }

    @PatchMapping("/{pharmacyId}/status")
    public PharmacyResponse updateStatus(
            @PathVariable UUID pharmacyId,
            @RequestParam String status
    ) {
        return pharmacyService.updateStatus(pharmacyId, status);
    }

    @PatchMapping("/{pharmacyId}/approve")
    public PharmacyResponse approve(@PathVariable UUID pharmacyId) {
        return pharmacyService.approve(pharmacyId);
    }

    @PatchMapping("/{pharmacyId}/reject")
    public PharmacyResponse reject(@PathVariable UUID pharmacyId) {
        return pharmacyService.reject(pharmacyId);
    }

    @PatchMapping("/{pharmacyId}/suspend")
    public PharmacyResponse suspend(@PathVariable UUID pharmacyId) {
        return pharmacyService.suspend(pharmacyId);
    }

    @GetMapping("/nearby")
    public List<PharmacyResponse> nearby(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam double radiusKm
    ) {
        return pharmacyService.searchNearby(lat, lng, radiusKm);
    }


}
