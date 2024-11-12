package com.gdgyonsei.otp.domains.badge.service;

import com.gdgyonsei.otp.domains.badge.model.Badge;
import com.gdgyonsei.otp.domains.badge.model.BadgeType;
import com.gdgyonsei.otp.domains.badge.repository.BadgeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    // Create
    public Badge createBadge(Long memberId, BadgeType badgeType, Integer consecutiveMonths, String specificYearMonth) {
        Badge badge = new Badge();
        badge.setMemberId(memberId);
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

        badge.setAcquiredDate(LocalDate.now());
        return badgeRepository.save(badge);
    }

    // Read (by ID)
    public Badge getBadgeById(Long id) {
        return badgeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge not found with id: " + id));
    }

    // Read (by Member ID)
    public List<Badge> getBadgesByMemberId(Long memberId) {
        return badgeRepository.findAllByMemberId(memberId);
    }

    // Update
    public Badge updateBadge(Long id, Badge updatedBadge) {
        Badge badge = getBadgeById(id);
        badge.setBadgeType(updatedBadge.getBadgeType());
        badge.setConsecutiveMonths(updatedBadge.getConsecutiveMonths());
        badge.setSpecificYearMonth(updatedBadge.getSpecificYearMonth());
        badge.setAcquiredDate(updatedBadge.getAcquiredDate());
        return badgeRepository.save(badge);
    }

    // Delete
    public void deleteBadgeById(Long id) {
        badgeRepository.deleteById(id);
    }
}
