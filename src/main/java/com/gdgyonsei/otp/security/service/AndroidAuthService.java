package com.gdgyonsei.otp.security.service;

import com.gdgyonsei.otp.domains.member.repository.MemberRepository;
import com.gdgyonsei.otp.security.DTO.AndroidVerifyingInfo;
import com.gdgyonsei.otp.security.util.JwtTokenProvider;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class AndroidAuthService {
    private final GoogleIdTokenVerifier tokenVerifier;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AndroidAuthService(GoogleIdTokenVerifier tokenVerifier, MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.tokenVerifier = tokenVerifier;
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AndroidVerifyingInfo generateToken(String email) {
        // findByEmail 사용하여 사용자 존재 여부 확인
        boolean isNewMember = memberRepository.findByEmail(email).isEmpty();

        // JWT 생성
        String accessToken = jwtTokenProvider.createToken(email);

        // SecurityContext에 인증 정보 저장
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return new AndroidVerifyingInfo(email, isNewMember, accessToken);
    }
}


