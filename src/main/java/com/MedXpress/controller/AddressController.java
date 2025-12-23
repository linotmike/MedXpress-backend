package com.MedXpress.controller;

import com.MedXpress.dto.address.AddressCreateRequest;
import com.MedXpress.dto.address.AddressResponse;
import com.MedXpress.service.address.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AddressResponse create(@Valid @RequestBody AddressCreateRequest request) {
        return addressService.create(request);
    }

    @GetMapping("/{addressId}")
    public AddressResponse getById(@PathVariable UUID addressId) {
        return addressService.getById(addressId);
    }

    @GetMapping
    public List<AddressResponse> listByUser(@RequestParam UUID userId) {
        return addressService.listByUser(userId);
    }

    @PatchMapping("/{addressId}/default")
    public AddressResponse setDefault(
            @PathVariable UUID addressId,
            @RequestParam UUID userId
    ) {
        return addressService.setDefault(userId, addressId);
    }
}
