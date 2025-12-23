package com.MedXpress.repository;

import com.MedXpress.entity.Pharmacy;
import com.MedXpress.util.PharmacyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyRepository extends JpaRepository<Pharmacy, UUID> {

    boolean existsByLicenseNumber(String licenseNumber);

    Optional<Pharmacy> findByLicenseNumber(String licenseNumber);

    List<Pharmacy> findByOwnerUser_Id(UUID ownerUserId);

    List<Pharmacy> findByStatus(PharmacyStatus status);
}
