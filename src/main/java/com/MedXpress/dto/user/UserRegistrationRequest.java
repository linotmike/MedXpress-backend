package com.MedXpress.dto.user;

import com.MedXpress.util.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequest {

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @Size(max = 150)
    private String email;

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @NotNull
    private UserRole role; // PATIENT, PHARMACY_ADMIN, RIDER, ADMIN
}
