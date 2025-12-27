package com.MedXpress.service.delivery;

import com.MedXpress.dto.delivery.DeliveryAssignRequest;
import com.MedXpress.dto.delivery.DeliveryResponse;
import com.MedXpress.dto.delivery.DeliveryStatusUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface DeliveryService {

    DeliveryResponse assign(DeliveryAssignRequest request);

    DeliveryResponse updateStatus(UUID deliveryId, DeliveryStatusUpdateRequest request);

    DeliveryResponse getById(UUID deliveryId);

    DeliveryResponse getByOrderId(UUID orderId);

    List<DeliveryResponse> listByRider(UUID riderId);
}
