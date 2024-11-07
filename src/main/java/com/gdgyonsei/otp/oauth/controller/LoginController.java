package com.gdgyonsei.otp.oauth.controller;

import com.gdgyonsei.otp.DTO.NewMemberInfoRequest;
import com.gdgyonsei.otp.model.Member;
import com.gdgyonsei.otp.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post-login")
public class LoginController {
    private final HttpSession session;
    private final MemberService memberService;

    public LoginController(HttpSession session, MemberService memberService) {
        this.session = session;
        this.memberService = memberService;
    }

    // 구글 인증 끝난 후 진입하는 포인트
    @GetMapping
    public ResponseEntity<String> postLogin() {
        if (Boolean.TRUE.equals(session.getAttribute("isNewMember"))) {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/post-login/askNewMemInfoToClient").build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/post-login/existingMember").build();
        }
    }

    // 신규 멤버인 경우 클라이언트에 추가적인 정보를 요청한다.
    @GetMapping("/askNewMemInfoToClient")
    public ResponseEntity<Map<String, String>> newMemberInfoRequest() {
        Map<String, String> response = new HashMap<>();
        Member newMember = (Member) session.getAttribute("newMember");
        if (newMember != null) {
            response.put("message", "Provide additional information for new member, including name, gender, and incomeLevel.");
            response.put("defaultName", newMember.getName());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "New member information is missing"));
        }
    }

    // 클라이언트는 신규 멤버의 추가적인 정보를 담은 요청을 이곳으로 날린다.
    @PostMapping("/newMemInfoReply")
    public ResponseEntity<String> newMemInfoProcess(@RequestBody NewMemberInfoRequest newMemberInfo) {
        Member newMember = (Member) session.getAttribute("newMember");
        if (newMember == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No new member information in session.");
        }

        newMember.setName(newMemberInfo.getName());

        // Gender 처리
        try {
            Member.Gender genderModified = Member.Gender.valueOf(newMemberInfo.getGender().toUpperCase());
            newMember.setGender(genderModified);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid gender");
        }

        // Income Level 처리
        try {
            Member.IncomeLevel incomeLevelModified = Member.IncomeLevel.valueOf(newMemberInfo.getIncomeLevel().toUpperCase());
            newMember.setIncomeLevel(incomeLevelModified);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid incomeLevel");
        }

        // 회원 정보 저장
        memberService.saveMember(newMember);
        return ResponseEntity.status(HttpStatus.CREATED).body("New member information has been successfully saved.");
    }

    @GetMapping("/existingMember")
    public ResponseEntity<String> existingMember() {
        return ResponseEntity.ok("Successfully logged in as an existing member.");
    }
}

