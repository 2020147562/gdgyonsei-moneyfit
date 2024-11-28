package com.gdgyonsei.otp.domains.member.controller;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.member.dto.NewMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.dto.UpdateMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.model.Member;
import com.gdgyonsei.otp.domains.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/android/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createMember(@RequestBody NewMemberInfoRequest request) {
        if (!request.getEmail().equals(getAuthenticatedEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Wrong email input. Input the email regarding of token"));
        }
        Member member = memberService.createMember(request);
        return ResponseEntity.ok(Map.of("message", "Member created successfully", "id", member.getId().toString()));
    }

    @GetMapping
    public ResponseEntity<Member> getMember() {
        String email = getAuthenticatedEmail();
        return ResponseEntity.ok(memberService.getMemberByEmail(email));
    }

    // Read (Find by ID)
    @GetMapping("/{id}")
    public Member getMemberById(@PathVariable Long id) {
        return memberService.getMemberById(id);
    }

    // Read All
    @GetMapping("/list")
    public List<Member> getAllMembers() {
        return memberService.getAllMembers();
    }

    // Update
    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> updateMember(@RequestBody UpdateMemberInfoRequest request) {
        String email = getAuthenticatedEmail();
        memberService.updateMember(email, request);
        return ResponseEntity.ok(Map.of("message", "Member updated successfully"));
    }

    // Delete
    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> deleteMemberById() {
        String email = getAuthenticatedEmail();
        memberService.deleteMemberByEmail(email);
        return ResponseEntity.ok(Map.of("message", "Member deleted successfully"));
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}



