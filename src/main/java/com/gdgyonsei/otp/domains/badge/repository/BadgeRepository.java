package com.gdgyonsei.otp.domains.badge.repository;

import com.gdgyonsei.otp.domains.badge.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findAllByMemberId(Long memberId); // 특정 유저의 모든 뱃지 조회
}
