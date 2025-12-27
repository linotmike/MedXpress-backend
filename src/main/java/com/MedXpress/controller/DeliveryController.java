package com.MedXpress.controller;

import com.MedXpress.dto.delivery.DeliveryAssignRequest;
import com.MedXpress.dto.delivery.DeliveryResponse;
import com.MedXpress.dto.delivery.DeliveryStatusUpdateRequest;
import com.MedXpress.service.delivery.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/assign")
    @ResponseStatus(HttpStatus.CREATED)
    public DeliveryResponse assign(@Valid @RequestBody DeliveryAssignRequest request) {
        return deliveryService.assign(request);
    }

    @PatchMapping("/{deliveryId}/status")
    public DeliveryResponse updateStatus(
            @PathVariable UUID deliveryId,
            @Valid @RequestBody DeliveryStatusUpdateRequest request
    ) {
        return deliveryService.updateStatus(deliveryId, request);
    }

    @GetMapping("/{deliveryId}")
    public DeliveryResponse getById(@PathVariable UUID deliveryId) {
        return deliveryService.getById(deliveryId);
    }

    @GetMapping("/by-order/{orderId}")
    public DeliveryResponse getByOrder(@PathVariable UUID orderId) {
        return deliveryService.getByOrderId(orderId);
    }

    @GetMapping
    public List<DeliveryResponse> listByRider(@RequestParam UUID riderId) {
        return deliveryService.listByRider(riderId);
    }
}
