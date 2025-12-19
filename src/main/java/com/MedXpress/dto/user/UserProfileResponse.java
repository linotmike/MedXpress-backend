package com.MedXpress.dto.user;

import com.MedXpress.util.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
public class UserProfileResponse {
    private UUID id;
    private String phoneNumber;
    private String fullName;
    private String email;
    private UserRole role;
    private boolean isActive;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
