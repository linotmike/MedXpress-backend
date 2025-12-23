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

    @Override
    public PharmacyResponse approve(UUID pharmacyId) {
        Pharmacy p = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        if (p.getStatus() == PharmacyStatus.APPROVED) {
            return PharmacyMapper.toResponse(p);
        }

        if (p.getStatus() == PharmacyStatus.SUSPENDED) {
            throw new BusinessException("Suspended pharmacies cannot be approved directly.");
        }

        p.setStatus(PharmacyStatus.APPROVED);
        return PharmacyMapper.toResponse(p);
    }

    @Override
    public PharmacyResponse reject(UUID pharmacyId) {
        Pharmacy p = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        if (p.getStatus() == PharmacyStatus.REJECTED) {
            return PharmacyMapper.toResponse(p);
        }

        if (p.getStatus() == PharmacyStatus.APPROVED) {
            throw new BusinessException("Approved pharmacies cannot be rejected. Suspend instead.");
        }

        p.setStatus(PharmacyStatus.REJECTED);
        return PharmacyMapper.toResponse(p);
    }

    @Override
    public PharmacyResponse suspend(UUID pharmacyId) {
        Pharmacy p = pharmacyRepository.findById(pharmacyId)
                .orElseThrow(() -> new NotFoundException("Pharmacy not found."));

        if (p.getStatus() == PharmacyStatus.SUSPENDED) {
            return PharmacyMapper.toResponse(p);
        }

        p.setStatus(PharmacyStatus.SUSPENDED);
        return PharmacyMapper.toResponse(p);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PharmacyResponse> searchNearby(double latitude, double longitude, double radiusKm) {

        if (radiusKm <= 0 || radiusKm > 50) {
            throw new BusinessException("radiusKm must be between 0 and 50.");
        }

        // Only show APPROVED pharmacies to users
        List<Pharmacy> approved = pharmacyRepository.findByStatus(PharmacyStatus.APPROVED);

        return approved.stream()
                .filter(p -> p.getLatitude() != null && p.getLongitude() != null)
                .filter(p -> distanceKm(latitude, longitude, p.getLatitude(), p.getLongitude()) <= radiusKm)
                .map(PharmacyMapper::toResponse)
                .toList();
    }

    private double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


}
