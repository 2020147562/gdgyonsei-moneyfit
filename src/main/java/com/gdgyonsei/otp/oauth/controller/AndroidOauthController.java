package com.gdgyonsei.otp.oauth.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AndroidOauthController {

    private final GoogleIdTokenVerifier verifier;

    public AndroidOauthController() {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                .setAudience(Collections.singletonList("YOUR_ANDROID_CLIENT_ID"))  // 안드로이드 클라이언트 ID
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();

                String userId = payload.getSubject(); // 사용자 고유 ID
                String email = payload.getEmail();   // 사용자 이메일

                // 사용자 인증 및 세션 처리 로직
                return ResponseEntity.ok(Map.of("message", "User authenticated", "userId", userId));
            } else {
                return ResponseEntity.status(401).body(Map.of("message","Invalid ID token"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Token verification failed"));
        }
    }
}
