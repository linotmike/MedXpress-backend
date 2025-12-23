package com.MedXpress.service.address;

import com.MedXpress.dto.address.AddressCreateRequest;
import com.MedXpress.dto.address.AddressResponse;

import java.util.List;
import java.util.UUID;

public interface AddressService {

    AddressResponse create(AddressCreateRequest request);

    AddressResponse setDefault(UUID userId, UUID addressId);

    List<AddressResponse> listByUser(UUID userId);

    AddressResponse getById(UUID addressId);
}
