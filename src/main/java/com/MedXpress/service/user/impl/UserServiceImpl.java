package com.MedXpress.service.user.impl;

import com.MedXpress.dto.user.UserProfileResponse;
import com.MedXpress.dto.user.UserRegistrationRequest;
import com.MedXpress.entity.User;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.UserMapper;
import com.MedXpress.repository.UserRepository;
import com.MedXpress.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserProfileResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new BusinessException("Phone number already registered.");
        }

        String hashed = passwordEncoder.encode(request.getPassword());
        User saved = userRepository.save(UserMapper.toEntity(request, hashed));
        return UserMapper.toProfile(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found."));
        return UserMapper.toProfile(u);
    }
}
