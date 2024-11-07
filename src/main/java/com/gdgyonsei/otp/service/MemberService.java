package com.gdgyonsei.otp.service;

import com.gdgyonsei.otp.model.Member;
import com.gdgyonsei.otp.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    @Transactional
    public void updateNameByEmail(String newName, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
        member.setName(newName);
        memberRepository.save(member);  // 자동 감지, save는 생략 가능
    }

    @Transactional
    public void updateGenderByEmail(String newGender, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
        member.setGender(Member.Gender.valueOf(newGender.toUpperCase()));
        memberRepository.save(member);  // 자동 감지
    }

    @Transactional
    public void updateIncomeLevelByEmail(String incomeLevel, String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
        member.setIncomeLevel(Member.IncomeLevel.valueOf(incomeLevel.toUpperCase()));
        memberRepository.save(member);  // 자동 감지
    }

    @Transactional
    public Map<String, String> getMemberInfoByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + email));
        Map<String, String> info = new HashMap<>();
        info.put("name", member.getName());
        info.put("email", member.getEmail());
        info.put("gender", member.getGender().toString());
        info.put("incomeLevel", member.getIncomeLevel().toString());
        return info;
    }
}

