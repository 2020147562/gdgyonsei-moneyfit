package com.gdgyonsei.otp.domains.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberInfoRequest {
    private String name;
    private String gender;
    private String incomeLevel;
    private String dateOfBirth; // yyyy-MM-dd 형식
    private String job;
}
