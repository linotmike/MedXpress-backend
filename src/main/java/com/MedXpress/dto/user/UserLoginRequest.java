package com.MedXpress.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    private String identifier; // email or phone

    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
}
