package com.gdgyonsei.otp.domains.member.controller;

import com.gdgyonsei.otp.domains.member.dto.NewMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.model.Member;
import com.gdgyonsei.otp.domains.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/android/member")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // Create
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createMember(@RequestBody NewMemberInfoRequest request) {
        Member member = memberService.createMember(request);
        return ResponseEntity.ok(Map.of("message", "Member created successfully", "id", member.getId().toString()));
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
    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateMember(@PathVariable Long id, @RequestBody NewMemberInfoRequest request) {
        memberService.updateMember(id, request);
        return ResponseEntity.ok(Map.of("message", "Member updated successfully"));
    }

    // Delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteMemberById(@PathVariable Long id) {
        memberService.deleteMemberById(id);
        return ResponseEntity.ok(Map.of("message", "Member deleted successfully"));
    }
}



