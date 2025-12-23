package com.MedXpress.repository;

import com.MedXpress.entity.PharmacyMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PharmacyMedicineRepository extends JpaRepository<PharmacyMedicine, UUID> {

    Optional<PharmacyMedicine> findByPharmacy_IdAndMedicine_Id(UUID pharmacyId, UUID medicineId);

    List<PharmacyMedicine> findByPharmacy_Id(UUID pharmacyId);

    List<PharmacyMedicine> findByMedicine_Id(UUID medicineId);

    boolean existsByPharmacy_IdAndMedicine_Id(UUID pharmacyId, UUID medicineId);
}
