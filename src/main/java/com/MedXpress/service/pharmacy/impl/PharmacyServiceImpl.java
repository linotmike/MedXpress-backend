package com.MedXpress.service.pharmacy.impl;

import com.MedXpress.dto.pharmacy.PharmacyCreateRequest;
import com.MedXpress.dto.pharmacy.PharmacyResponse;
import com.MedXpress.entity.Pharmacy;
import com.MedXpress.entity.User;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.PharmacyMapper;
import com.MedXpress.repository.PharmacyRepository;
import com.MedXpress.repository.UserRepository;
import com.MedXpress.service.pharmacy.PharmacyService;
import com.MedXpress.util.PharmacyStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyRepository pharmacyRepository;
    private final UserRepository userRepository;

    public PharmacyServiceImpl(
            PharmacyRepository pharmacyRepository,
            UserRepository userRepository
    ) {
        this.pharmacyRepository = pharmacyRepository;
        this.userRepository = userRepository;
    }

    @Override
    public PharmacyResponse create(PharmacyCreateRequest request) {

        if (pharmacyRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            throw new BusinessException("Pharmacy with this license number already exists.");
        }

        User owner = userRepository.findById(request.getOwnerUserId())
                .orElseThrow(() -> new NotFoundException("Owner user not found."));

        Pharmacy saved = pharmacyRepository.save(PharmacyMapper.toEntity(request, owner));
        return PharmacyMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public PharmacyResponse getById(UUID pharmacyId) {
        Pharmacy p = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));
        return PharmacyMapper.toResponse(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyResponse> listAll() {
        return pharmacyRepository.findAll()
                .stream()
                .map(PharmacyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyResponse> listByOwner(UUID ownerUserId) {
        return pharmacyRepository.findByOwnerUser_Id(ownerUserId)
                .stream()
                .map(PharmacyMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyResponse> listByStatus(String status) {
        PharmacyStatus ps;
        try {
            ps = PharmacyStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid pharmacy status.");
        }

        return pharmacyRepository.findByStatus(ps)
                .stream()
                .map(PharmacyMapper::toResponse)
                .toList();
    }

    @Override
    public PharmacyResponse updateStatus(UUID pharmacyId, String status) {

        Pharmacy p = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        PharmacyStatus ps;
        try {
            ps = PharmacyStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid pharmacy status.");
        }

        p.setStatus(ps);
        return PharmacyMapper.toResponse(p);
    }
}
