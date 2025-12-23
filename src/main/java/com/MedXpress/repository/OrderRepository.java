package com.MedXpress.repository;

import com.MedXpress.entity.Order;
import com.MedXpress.util.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findByPatient_Id(UUID patientId);

    List<Order> findByPharmacy_Id(UUID pharmacyId);

    List<Order> findByStatus(OrderStatus status);
}
