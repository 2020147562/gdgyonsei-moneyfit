package com.gdgyonsei.otp.domains.member.service;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.member.dto.NewMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.dto.UpdateMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.model.Gender;
import com.gdgyonsei.otp.domains.member.model.IncomeLevel;
import com.gdgyonsei.otp.domains.member.model.Job;
import com.gdgyonsei.otp.domains.member.model.Member;
import com.gdgyonsei.otp.domains.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final BadgeService badgeService;
    private final BadgeOwnershipService badgeOwnershipService;

    // Create
    @Transactional
    public Member createMember(NewMemberInfoRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        member.setEmail(request.getEmail());
        member.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        member.setIncomeLevel(IncomeLevel.valueOf(request.getIncomeLevel().toUpperCase()));
        member.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        member.setJob(Job.valueOf(request.getJob().toUpperCase()));

        String email = request.getEmail();
        String badgeTypeString = "SIGN_UP_COMPLETE";
        badgeService.createBadge(email, badgeTypeString, null, null);
        badgeOwnershipService.createBadgeOwnershipService(email);

        return memberRepository.save(member);
    }

    // Read (Find by ID)
    @Transactional(readOnly = true)
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    // Read (Find by email in token context)
    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
    }

    // Read All
    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Update (Partial Update)
    @Transactional
    public void updateMember(String email, UpdateMemberInfoRequest request) {
        Member member = getMemberByEmail(email);
        member.setName(request.getName());
        member.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        member.setIncomeLevel(IncomeLevel.valueOf(request.getIncomeLevel().toUpperCase()));
        member.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
        member.setJob(Job.valueOf(request.getJob().toUpperCase()));
        memberRepository.save(member);
    }

    // Delete
    @Transactional
    public void deleteMemberByEmail(String email) {
        Member member = getMemberByEmail(email);
        Long id = member.getId();
        memberRepository.deleteById(id);
    }
}



