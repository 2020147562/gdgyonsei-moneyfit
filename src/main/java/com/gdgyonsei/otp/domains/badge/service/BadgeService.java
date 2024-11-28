package com.gdgyonsei.otp.domains.badge.service;

import com.gdgyonsei.otp.domains.badge.dto.BadgeCreateRequest;
import com.gdgyonsei.otp.domains.badge.model.Badge;
import com.gdgyonsei.otp.domains.badge.model.BadgeType;
import com.gdgyonsei.otp.domains.badge.repository.BadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    // Create
    @Transactional
    public Badge createBadge(String memberEmail, String badgeTypeString, Integer consecutiveMonths, String specificYearMonth) {
        BadgeType badgeType = BadgeType.valueOf(badgeTypeString);
        Badge badge = new Badge();
        badge.setMemberEmail(memberEmail);
        badge.setBadgeType(badgeType);

        // BadgeType에 따라 필드 설정
        switch (badgeType) {
            case CONSECUTIVE_EXPENSE_OBJECTIVE_ACHIEVE:
                badge.setConsecutiveMonths(consecutiveMonths != null ? consecutiveMonths : 0);
                badge.setSpecificYearMonth(null);  // 다른 필드는 초기화
                break;
            case SPECIFIC_MONTH_EXPENSE_OBJECTIVE_ACHIEVE:
                badge.setSpecificYearMonth(specificYearMonth);
                badge.setConsecutiveMonths(0);  // 다른 필드는 초기화
                break;
            default:
                badge.setConsecutiveMonths(0);
                badge.setSpecificYearMonth(null);  // 기본 필드 초기화
        }

        badge.setAcquiredDateTime(LocalDateTime.now());
        return badgeRepository.save(badge);
    }

    // Read (by ID)
    @Transactional(readOnly = true)
    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found with id: " + id));
    }

    // Read (by Member Email)
    @Transactional(readOnly = true)
    public List<Badge> getBadgesByMemberEmail(String memberEmail) {
        return badgeRepository.findAllByMemberEmail(memberEmail);
    }

    // Update
    @Transactional
    public Badge updateBadge(Long id, BadgeCreateRequest updatedBadge) {
        Badge badge = getBadgeById(id);
        badge.setBadgeType(BadgeType.valueOf(updatedBadge.getBadgeTypeString()));
        badge.setConsecutiveMonths(updatedBadge.getConsecutiveMonths());
        badge.setSpecificYearMonth(updatedBadge.getSpecificYearMonth());
        return badgeRepository.save(badge);
    }

    // Delete
    @Transactional
    public void deleteBadgeById(Long id) {
        badgeRepository.deleteById(id);
    }
}
