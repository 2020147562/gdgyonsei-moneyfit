package com.gdgyonsei.otp.domains.spending.controller;

import com.gdgyonsei.otp.domains.spending.dto.SpendingCreateRequest;
import com.gdgyonsei.otp.domains.spending.dto.SpendingUpdateRequest;
import com.gdgyonsei.otp.domains.spending.model.Spending;
import com.gdgyonsei.otp.domains.spending.service.SpendingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/android/spending")
public class SpendingController {
    private final SpendingService spendingService;

    public SpendingController(SpendingService spendingService) {
        this.spendingService = spendingService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createSpending(@RequestBody SpendingCreateRequest request) {
        String email = getAuthenticatedEmail();
        Spending spending = spendingService.createSpending(request, email);
        return ResponseEntity.ok(Map.of("message", "Spending created successfully", "id", spending.getId().toString()));
    }

    @GetMapping("/{id}")
    public Spending getSpendingById(@PathVariable Long id) {
        return spendingService.getSpendingById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateSpending(@PathVariable Long id, @RequestBody SpendingUpdateRequest request) {
        spendingService.updateSpendingById(id, request);
        return ResponseEntity.ok(Map.of("message", "Spending updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteSpending(@PathVariable Long id) {
        spendingService.deleteSpendingById(id);
        return ResponseEntity.ok(Map.of("message", "Spending deleted successfully"));
    }

    @GetMapping("/list")
    public List<Spending> getSpendingList() {
        String email = getAuthenticatedEmail();
        return spendingService.getSpendingListByMemberEmail(email);
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

