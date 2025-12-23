package com.MedXpress.service.pharmacy;

import com.MedXpress.dto.pharmacy.PharmacyCreateRequest;
import com.MedXpress.dto.pharmacy.PharmacyResponse;

import java.util.List;
import java.util.UUID;

public interface PharmacyService {

    PharmacyResponse create(PharmacyCreateRequest request);

    PharmacyResponse getById(UUID pharmacyId);

    List<PharmacyResponse> listAll();

    List<PharmacyResponse> listByOwner(UUID ownerUserId);

    List<PharmacyResponse> listByStatus(String status);

    PharmacyResponse updateStatus(UUID pharmacyId, String status);

    PharmacyResponse approve(UUID pharmacyId);

    PharmacyResponse reject(UUID pharmacyId);

    PharmacyResponse suspend(UUID pharmacyId);

}
