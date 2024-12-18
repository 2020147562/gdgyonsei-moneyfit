package com.gdgyonsei.otp.domains.badge.controller;

import com.gdgyonsei.otp.domains.badge.dto.BadgeCreateRequest;
import com.gdgyonsei.otp.domains.badge.model.Badge;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/android/badges")
public class BadgeController {
    private final BadgeService badgeService;

    // Create
    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBadge(@Valid @RequestBody BadgeCreateRequest request) {
        Badge badge = badgeService.createBadge(
                getAuthenticatedEmail(),
                request.getBadgeTypeString(),
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

    // Read (with list)
    @GetMapping("/list")
    public ResponseEntity<List<Badge>> getBadgesByMemberEmail() {
        String email = getAuthenticatedEmail();
        List<Badge> badges = badgeService.getBadgesByMemberEmail(email);
        return ResponseEntity.ok(badges);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Badge> updateBadge(@PathVariable Long id, @Valid @RequestBody BadgeCreateRequest updatedBadge) {
        Badge badge = badgeService.updateBadge(id, updatedBadge);
        return ResponseEntity.ok(badge);
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteBadge(@PathVariable Long id) {
        badgeService.deleteBadgeById(id);
        return ResponseEntity.ok(Map.of("message", "Badge deleted successfully"));
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
