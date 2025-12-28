package com.MedXpress.service.user.impl;

import com.MedXpress.dto.user.AuthResponse;
import com.MedXpress.dto.user.UserLoginRequest;
import com.MedXpress.dto.user.UserProfileResponse;
import com.MedXpress.dto.user.UserRegistrationRequest;
import com.MedXpress.entity.User;
import com.MedXpress.exception.BusinessException;
import com.MedXpress.exception.NotFoundException;
import com.MedXpress.mapper.UserMapper;
import com.MedXpress.repository.UserRepository;
import com.MedXpress.security.JwtUtil;
import com.MedXpress.service.user.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
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

    @Override
    public AuthResponse login(UserLoginRequest request) {
        String identifier = request.getIdentifier().trim();

        Optional<User> userOpt;
        if (identifier.contains("@")) {
            userOpt = userRepository.findByEmailIgnoreCase(identifier);
        } else {
            userOpt = userRepository.findByPhoneNumber(identifier);
        }

        User user = userOpt.orElseThrow(() -> new NotFoundException("Invalid credentials."));

        if (!Boolean.TRUE.equals(user.isActive())) {
            throw new BusinessException("Account is inactive.");
        }

        boolean ok = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!ok) {
            throw new NotFoundException("Invalid credentials.");
        }

        // Create JWT (subject can be userId; include role as claim)
        String token = jwtUtil.generateToken(user.getId().toString(), user.getRole().name());

        return new AuthResponse(token, UserMapper.toProfile(user));
    }


}
