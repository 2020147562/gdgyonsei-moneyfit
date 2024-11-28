package com.gdgyonsei.otp.security.util;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${SECRET_KEY_FOR_SERVER_JWT}")
    private String secretKeyEnv;

    private SecretKey secretKey;
    private final long validityInMilliseconds = 36000000; // 10시간 유효기간

    @PostConstruct
    protected void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyEnv);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    /**
     * JWT 생성 메서드
     */
    public String createToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(email) // 사용자 식별자 (email)
                .setIssuedAt(now)  // 토큰 발급 시간
                .setExpiration(expiryDate) // 만료 시간
                .signWith(secretKey) // Secret Key로 서명
                .compact();
    }

    /**
     * JWT 유효성 검증 메서드
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey) // Secret Key로 서명 검증
                    .build()
                    .parseSignedClaims(token); // 파싱 및 검증
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // JWT가 유효하지 않으면 예외 발생
            System.err.println("Invalid JWT token: " + e.getMessage());
            return false;
        }
    }

    /**
     * JWT에서 클레임 추출 메서드
     */
    public Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            // 로그 추가
            System.err.println("Failed to extract claims from token: " + e.getMessage());
            throw new IllegalArgumentException("Invalid token", e);
        }
    }
}






