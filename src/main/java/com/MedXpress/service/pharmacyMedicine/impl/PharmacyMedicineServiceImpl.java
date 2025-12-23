package com.MedXpress.service.pharmacyMedicine.impl;

import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineResponse;
import com.MedXpress.dto.pharmacyMedicine.PharmacyMedicineUpsertRequest;
import com.MedXpress.entity.Medicine;
import com.MedXpress.entity.Pharmacy;
import com.MedXpress.entity.PharmacyMedicine;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.PharmacyMedicineMapper;
import com.MedXpress.repository.MedicineRepository;
import com.MedXpress.repository.PharmacyMedicineRepository;
import com.MedXpress.repository.PharmacyRepository;
import com.MedXpress.service.pharmacyMedicine.PharmacyMedicineService;
import com.MedXpress.util.PharmacyStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PharmacyMedicineServiceImpl implements PharmacyMedicineService {

    private final PharmacyMedicineRepository pharmacyMedicineRepository;
    private final PharmacyRepository pharmacyRepository;
    private final MedicineRepository medicineRepository;

    public PharmacyMedicineServiceImpl(
            PharmacyMedicineRepository pharmacyMedicineRepository,
            PharmacyRepository pharmacyRepository,
            MedicineRepository medicineRepository
    ) {
        this.pharmacyMedicineRepository = pharmacyMedicineRepository;
        this.pharmacyRepository = pharmacyRepository;
        this.medicineRepository = medicineRepository;
    }

    @Override
    public PharmacyMedicineResponse upsert(PharmacyMedicineUpsertRequest request) {

        if (request.getPrice().signum() < 0) {
            throw new BusinessException("Price must be >= 0.");
        }
        if (request.getStockQuantity() < 0) {
            throw new BusinessException("Stock quantity must be >= 0.");
        }

        Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        if (pharmacy.getStatus() !=  PharmacyStatus.APPROVED) {
            throw new BusinessException("Pharmacy must be APPROVED to manage inventory.");
        }

        Medicine medicine = medicineRepository.findById(request.getMedicineId())
                .orElseThrow(() -> new NotFoundException("Medicine not found."));

        PharmacyMedicine pm = pharmacyMedicineRepository
                .findByPharmacy_IdAndMedicine_Id(
                        request.getPharmacyId(),
                        request.getMedicineId()
                )
                .orElseGet(() -> PharmacyMedicine.builder()
                        .pharmacy(pharmacy)
                        .medicine(medicine)
                        .build());

        pm.setPrice(request.getPrice());
        pm.setStockQuantity(request.getStockQuantity());

        if (request.getIsAvailable() != null) {
            pm.setAvailable(request.getIsAvailable());
        } else {
            pm.setAvailable(request.getStockQuantity() > 0);
        }

        return PharmacyMedicineMapper.toResponse(
                pharmacyMedicineRepository.save(pm)
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyMedicineResponse getById(UUID pharmacyMedicineId) {
        return pharmacyMedicineRepository.findById(pharmacyMedicineId)
                .map(PharmacyMedicineMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Pharmacy medicine not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicineResponse> listByPharmacy(UUID pharmacyId) {
        return pharmacyMedicineRepository.findByPharmacy_Id(pharmacyId)
                .stream()
                .map(PharmacyMedicineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyMedicineResponse> listByMedicine(UUID medicineId) {
        return pharmacyMedicineRepository.findByMedicine_Id(medicineId)
                .stream()
                .map(PharmacyMedicineMapper::toResponse)
                .toList();
    }
}
