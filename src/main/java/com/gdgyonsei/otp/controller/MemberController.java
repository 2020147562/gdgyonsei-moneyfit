package com.gdgyonsei.otp.controller;

import com.gdgyonsei.otp.DTO.GenderUpdateRequest;
import com.gdgyonsei.otp.DTO.IncomeLevelUpdateRequest;
import com.gdgyonsei.otp.DTO.NameUpdateRequest;
import com.gdgyonsei.otp.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/member")  // 기본 경로 설정
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/changeName")
    public String changeName(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody NameUpdateRequest request) {
        String email = oAuth2User.getAttribute("email");
        memberService.updateNameByEmail(request.getName(), email);
        return "Name updated successfully";
    }

    @PostMapping("/changeGender")
    public String changeGender(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody GenderUpdateRequest request) {
        String email = oAuth2User.getAttribute("email");
        memberService.updateGenderByEmail(request.getGender(), email);
        return "Gender updated successfully";
    }

    @PostMapping("/changeIncomeLevel")
    public String changeIncomeLevel(@AuthenticationPrincipal OAuth2User oAuth2User, @RequestBody IncomeLevelUpdateRequest request) {
        String email = oAuth2User.getAttribute("email");
        memberService.updateIncomeLevelByEmail(request.getIncomeLevel(), email);
        return "Income level updated successfully";
    }

    @GetMapping("/getMemberInfo")
    public ResponseEntity<Map<String, String>> getName(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        Map<String, String> response = memberService.getMemberInfoByEmail(email);
        return ResponseEntity.ok().body(response);
    }
}

