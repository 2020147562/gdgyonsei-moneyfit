package com.gdgyonsei.otp.domains.spending.controller;

import com.gdgyonsei.otp.domains.spending.dto.SpendingCreateRequest;
import com.gdgyonsei.otp.domains.spending.dto.SpendingUpdateRequest;
import com.gdgyonsei.otp.domains.spending.model.Spending;
import com.gdgyonsei.otp.domains.spending.model.UpperCategorySummary;
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

    // Spending의 id로 검색
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

    @GetMapping("/latest-spendings/{num}/{page}")
    public ResponseEntity<List<Spending>> getLatestSpendings(@PathVariable int num, @PathVariable int page) {
        String email = getAuthenticatedEmail(); // 인증된 사용자 이메일 가져오기
        List<Spending> response = spendingService.getTopNSpendings(email, num, page);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category-monthly-summary/{year}/{month}")
    public ResponseEntity<List<UpperCategorySummary>> getCategorySummary(
            @PathVariable int year,
            @PathVariable int month) {
        String email = getAuthenticatedEmail(); // 인증된 사용자 이메일 가져오기
        List<UpperCategorySummary> response = spendingService.getMonthlyExpenseByCategory(email, year, month);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly-expense/{year}/{month}")
    public ResponseEntity<Integer> getTotalExpenseByYearAndMonth(
            @PathVariable int year,
            @PathVariable int month) {
        String email = getAuthenticatedEmail();
        Integer totalExpense = spendingService.getTotalExpenseByYearAndMonth(email, year, month);
        return ResponseEntity.ok(totalExpense);
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

