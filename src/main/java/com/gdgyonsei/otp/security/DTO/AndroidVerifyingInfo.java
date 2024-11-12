package com.gdgyonsei.otp.security.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // 기본 생성자
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
public class AndroidVerifyingInfo {
    private String name;
    private String email;
    private boolean isNewMember;
    private String accessToken;
}

