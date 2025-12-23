package com.MedXpress.dto.order;

import com.MedXpress.util.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderCreateRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private UUID pharmacyId;

    @NotNull
    private UUID addressId;

    private PaymentMethod paymentMethod; // optional, defaults CASH_ON_DELIVERY

    private BigDecimal deliveryFee; // optional, defaults 0

    @Valid
    @NotNull
    private List<OrderItemCreateRequest> items;

    @Data
    public static class OrderItemCreateRequest {

        @NotNull
        private UUID pharmacyMedicineId;

        @Min(1)
        private int quantity;
    }
}
