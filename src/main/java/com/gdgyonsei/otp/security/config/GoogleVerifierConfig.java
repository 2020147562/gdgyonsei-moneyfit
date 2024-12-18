package com.gdgyonsei.otp.security.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleVerifierConfig {

    @Value("${ANDROID_GOOGLE_CLIENT_ID}")
    private String androidGoogleClientId;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        return new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), // HTTP Transport 설정
                GsonFactory.getDefaultInstance() // GsonFactory 사용
        )
                .setAudience(Collections.singletonList(androidGoogleClientId)) // 클라이언트 ID 설정
                .build();
    }
}

