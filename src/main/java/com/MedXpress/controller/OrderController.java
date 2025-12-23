package com.MedXpress.controller;

import com.MedXpress.dto.order.OrderCreateRequest;
import com.MedXpress.dto.order.OrderResponse;
import com.MedXpress.service.order.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody OrderCreateRequest request) {
        return orderService.create(request);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getById(@PathVariable UUID orderId) {
        return orderService.getById(orderId);
    }

    @GetMapping
    public List<OrderResponse> list(
            @RequestParam(required = false) UUID patientId,
            @RequestParam(required = false) UUID pharmacyId
    ) {
        if (patientId != null) return orderService.listByPatient(patientId);
        if (pharmacyId != null) return orderService.listByPharmacy(pharmacyId);
        throw new IllegalArgumentException("Provide patientId or pharmacyId.");
    }
}
