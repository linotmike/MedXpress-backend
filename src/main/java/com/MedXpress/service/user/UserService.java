package com.MedXpress.service.user;

import com.MedXpress.dto.user.AuthResponse;
import com.MedXpress.dto.user.UserLoginRequest;
import com.MedXpress.dto.user.UserProfileResponse;
import com.MedXpress.dto.user.UserRegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    UserProfileResponse register(UserRegistrationRequest request);
    UserProfileResponse getProfile(UUID userId);
    AuthResponse login(UserLoginRequest request);

}