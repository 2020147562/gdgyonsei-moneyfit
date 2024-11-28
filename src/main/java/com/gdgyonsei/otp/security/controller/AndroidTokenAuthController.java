package com.gdgyonsei.otp.security.controller;

import com.gdgyonsei.otp.security.DTO.AndroidVerifyingInfo;
import com.gdgyonsei.otp.security.DTO.LoginRequest;
import com.gdgyonsei.otp.security.service.AndroidAuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/android/auth")
public class AndroidTokenAuthController {
    private final AndroidAuthService authService;

    public AndroidTokenAuthController(AndroidAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) {
        String email = request.getEmail();
        AndroidVerifyingInfo info = authService.generateToken(email);
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }
}
