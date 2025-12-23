package com.MedXpress.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID id;

    private UUID patientId;
    private UUID pharmacyId;
    private UUID addressId;

    private String paymentMethod;
    private String status;

    private BigDecimal deliveryFee;
    private BigDecimal totalAmount;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    private List<OrderItemResponse> items;
}
