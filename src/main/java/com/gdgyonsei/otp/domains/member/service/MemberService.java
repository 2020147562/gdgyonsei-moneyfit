package com.gdgyonsei.otp.domains.member.service;

import com.gdgyonsei.otp.domains.member.dto.NewMemberInfoRequest;
import com.gdgyonsei.otp.domains.member.model.Gender;
import com.gdgyonsei.otp.domains.member.model.IncomeLevel;
import com.gdgyonsei.otp.domains.member.model.Job;
import com.gdgyonsei.otp.domains.member.model.Member;
import com.gdgyonsei.otp.domains.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

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
        return memberRepository.save(member);
    }

    // Read (Find by ID)
    @Transactional
    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    // Read All
    @Transactional
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Update (Partial Update)
    @Transactional
    public void updateMember(Long id, NewMemberInfoRequest request) {
        Member member = getMemberById(id);
        member.setName(request.getName());
        member.setGender(Gender.valueOf(request.getGender().toUpperCase()));
        member.setIncomeLevel(IncomeLevel.valueOf(request.getIncomeLevel().toUpperCase()));
        memberRepository.save(member);
    }

    // Delete
    @Transactional
    public void deleteMemberById(Long id) {
        memberRepository.deleteById(id);
    }
}



