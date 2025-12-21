package com.MedXpress.service.medicine.impl;

import com.MedXpress.dto.medicine.MedicineCreateRequest;
import com.MedXpress.dto.medicine.MedicineResponse;
import com.MedXpress.entity.Medicine;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.MedicineMapper;
import com.MedXpress.repository.MedicineRepository;
import com.MedXpress.service.medicine.MedicineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    @Override
    public MedicineResponse create(MedicineCreateRequest request) {
        Medicine saved = medicineRepository.save(MedicineMapper.toEntity(request));
        return MedicineMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public MedicineResponse getById(UUID medicineId) {
        Medicine m = medicineRepository.findById(medicineId)
                .orElseThrow(() -> new NotFoundException("Medicine not found."));
        return MedicineMapper.toResponse(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> searchByName(String name) {
        return medicineRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicineResponse> listAll() {
        return medicineRepository.findAll()
                .stream()
                .map(MedicineMapper::toResponse)
                .toList();
    }
}
