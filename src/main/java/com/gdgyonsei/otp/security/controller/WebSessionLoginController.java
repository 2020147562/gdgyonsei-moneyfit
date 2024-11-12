package com.gdgyonsei.otp.security.controller;

import com.gdgyonsei.otp.domains.member.dto.NewMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.model.Member;
import com.gdgyonsei.otp.domains.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/post-login")
public class WebSessionLoginController {
    private final HttpSession session;
    private final MemberService memberService;

    public WebSessionLoginController(HttpSession session, MemberService memberService) {
        this.session = session;
        this.memberService = memberService;
    }

    @GetMapping
    public ResponseEntity<String> postLogin() {
        if (Boolean.TRUE.equals(session.getAttribute("isNewMember"))) {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/post-login/askNewMemInfoToClient").build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).header("Location", "/post-login/existingMember").build();
        }
    }

    @GetMapping("/askNewMemInfoToClient")
    public ResponseEntity<Map<String, String>> newMemberInfoRequest() {
        Member newMember = (Member) session.getAttribute("newMember");
        if (newMember != null) {
            return ResponseEntity.ok(Map.of(
                    "message", "Provide additional information for new member, including name, gender, incomeLevel, dateOfBirth, and job.",
                    "defaultName", newMember.getName()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "New member information is missing"));
        }
    }

    @PostMapping("/newMemInfoReply")
    public ResponseEntity<String> newMemInfoProcess(@RequestBody NewMemberInfoRequest newMemberInfo) {
        try {
            // Service 계층을 통해 Member 생성 및 저장
            Member savedMember = memberService.createMember(newMemberInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("New member information has been successfully saved. ID: " + savedMember.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid input: " + e.getMessage());
        }
    }

    @GetMapping("/existingMember")
    public ResponseEntity<String> existingMember() {
        return ResponseEntity.ok("Successfully logged in as an existing member.");
    }
}



