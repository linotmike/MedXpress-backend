package com.MedXpress.service.address.AddressServiceImpl;

import com.MedXpress.dto.address.AddressCreateRequest;
import com.MedXpress.dto.address.AddressResponse;
import com.MedXpress.entity.Address;
import com.MedXpress.entity.User;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.AddressMapper;
import com.MedXpress.repository.AddressRepository;
import com.MedXpress.repository.UserRepository;
import com.MedXpress.service.address.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AddressResponse create(AddressCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found."));

        Address saved = addressRepository.save(AddressMapper.toEntity(request, user));

        // If requested default, enforce single default
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            setDefault(user.getId(), saved.getId());
            // re-read to reflect correct default flags
            saved = addressRepository.findById(saved.getId())
                    .orElseThrow(() -> new NotFoundException("Address not found."));
        }

        return AddressMapper.toResponse(saved);
    }

    @Override
    public AddressResponse setDefault(UUID userId, UUID addressId) {

        Address target = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found."));

        if (!target.getUser().getId().equals(userId)) {
            throw new NotFoundException("Address not found for this user.");
        }

        // unset existing default if exists
        addressRepository.findByUser_IdAndIsDefaultTrue(userId).ifPresent(existing -> {
            if (!existing.getId().equals(addressId)) {
                existing.setDefault(false);
                addressRepository.save(existing);
            }
        });

        target.setDefault(true);
        return AddressMapper.toResponse(addressRepository.save(target));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> listByUser(UUID userId) {
        return addressRepository.findByUser_Id(userId)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AddressResponse getById(UUID addressId) {
        Address a = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found."));
        return AddressMapper.toResponse(a);
    }
}
