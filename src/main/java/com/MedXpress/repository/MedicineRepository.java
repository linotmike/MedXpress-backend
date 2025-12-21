package com.MedXpress.repository;

import com.MedXpress.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {

    List<Medicine> findByNameContainingIgnoreCase(String name);
}
