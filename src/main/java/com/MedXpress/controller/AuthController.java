package com.MedXpress.controller;

import com.MedXpress.dto.user.AuthResponse;
import com.MedXpress.dto.user.UserLoginRequest;
import com.MedXpress.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userService.login(request);
    }
}
