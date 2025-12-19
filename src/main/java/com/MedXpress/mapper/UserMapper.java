package com.MedXpress.mapper;

import com.MedXpress.dto.user.UserProfileResponse;
import com.MedXpress.dto.user.UserRegistrationRequest;
import com.MedXpress.entity.User;

public class UserMapper {

    private UserMapper() {}

    public static User toEntity(UserRegistrationRequest req, String passwordHash) {
        return User.builder()
                .phoneNumber(req.getPhoneNumber())
                .fullName(req.getFullName())
                .email(req.getEmail())
                .passwordHash(passwordHash)
                .role(req.getRole())
                .isActive(true)
                .build();
    }

    public static UserProfileResponse toProfile(User u) {
        return UserProfileResponse.builder()
                .id(u.getId())
                .phoneNumber(u.getPhoneNumber())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .role(u.getRole())
                .isActive(u.isActive())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}
