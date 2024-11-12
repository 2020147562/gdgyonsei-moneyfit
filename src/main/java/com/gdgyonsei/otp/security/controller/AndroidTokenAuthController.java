package com.gdgyonsei.otp.security.controller;

import com.gdgyonsei.otp.security.DTO.AndroidVerifyingInfo;
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
    public ResponseEntity<?> authenticateUser(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Authorization header format");
        }
        String token = authorizationHeader.replace("Bearer ", "");
        AndroidVerifyingInfo info = authService.verifyTokenAndAuthenticate(token);
        return ResponseEntity.status(HttpStatus.OK).body(info);
    }
}
