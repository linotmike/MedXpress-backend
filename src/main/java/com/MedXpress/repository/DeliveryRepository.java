package com.MedXpress.repository;

import com.MedXpress.entity.Delivery;
import com.MedXpress.util.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByOrder_Id(UUID orderId);

    List<Delivery> findByRider_Id(UUID riderId);

    List<Delivery> findByStatus(DeliveryStatus status);
}
