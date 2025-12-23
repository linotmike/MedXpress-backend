package com.MedXpress.service.order;

import com.MedXpress.dto.order.OrderCreateRequest;
import com.MedXpress.dto.order.OrderResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(OrderCreateRequest request);

    OrderResponse getById(UUID orderId);

    List<OrderResponse> listByPatient(UUID patientId);

    List<OrderResponse> listByPharmacy(UUID pharmacyId);
}
