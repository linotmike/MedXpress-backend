package com.MedXpress.controller;

import com.MedXpress.dto.user.UserProfileResponse;
import com.MedXpress.dto.user.UserRegistrationRequest;
import com.MedXpress.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse register(@Valid @RequestBody UserRegistrationRequest request) {
        return userService.register(request);
    }

    @GetMapping("/{userId}")
    public UserProfileResponse getProfile(@PathVariable UUID userId) {
        return userService.getProfile(userId);
    }
}
