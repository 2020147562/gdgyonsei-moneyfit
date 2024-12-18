package com.gdgyonsei.otp.domains.badge.repository;

import com.gdgyonsei.otp.domains.badge.model.BadgeOwnership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeOwnershipRepository extends JpaRepository<BadgeOwnership, Long> {
    BadgeOwnership findByMemberEmail(String memberEmail);
    void deleteByMemberEmail(String memberEmail);
}
