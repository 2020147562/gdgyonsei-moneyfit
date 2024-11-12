package com.gdgyonsei.otp.domains.badge.controller;

import com.gdgyonsei.otp.domains.badge.dto.BadgeCreateRequest;
import com.gdgyonsei.otp.domains.badge.model.Badge;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/badges")
public class BadgeController {
    private final BadgeService badgeService;

    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    // Create
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBadge(@RequestBody BadgeCreateRequest request) {
        Badge badge = badgeService.createBadge(
                request.getMemberId(),
                request.getBadgeType(),
                request.getConsecutiveMonths() != null ? request.getConsecutiveMonths() : 0,
                request.getSpecificYearMonth() != null ? request.getSpecificYearMonth() : null  // null 안전 처리
        );
        return ResponseEntity.ok(Map.of("message", "Badge created successfully", "id", badge.getId().toString()));
    }

    // Read (by ID)
    @GetMapping("/{id}")
    public ResponseEntity<Badge> getBadgeById(@PathVariable Long id) {
        Badge badge = badgeService.getBadgeById(id);
        return ResponseEntity.ok(badge);
    }

    // Read (by Member ID)
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Badge>> getBadgesByMemberId(@PathVariable Long memberId) {
        List<Badge> badges = badgeService.getBadgesByMemberId(memberId);
        return ResponseEntity.ok(badges);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Badge> updateBadge(@PathVariable Long id, @RequestBody Badge updatedBadge) {
        Badge badge = badgeService.updateBadge(id, updatedBadge);
        return ResponseEntity.ok(badge);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBadge(@PathVariable Long id) {
        badgeService.deleteBadgeById(id);
        return ResponseEntity.ok(Map.of("message", "Badge deleted successfully"));
    }
}
