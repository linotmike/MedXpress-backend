package com.MedXpress.repository;

import com.MedXpress.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUser_Id(UUID userId);

    Optional<Address> findByUser_IdAndIsDefaultTrue(UUID userId);
}
