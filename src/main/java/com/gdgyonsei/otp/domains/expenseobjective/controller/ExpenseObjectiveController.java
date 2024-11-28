package com.gdgyonsei.otp.domains.expenseobjective.controller;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.service.ExpenseObjectiveService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/android/expense-objective")
public class ExpenseObjectiveController {
    private final ExpenseObjectiveService service;

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createExpenseObjective(@RequestBody ExpenseObjectiveCreateRequest request) {
        String email = getAuthenticatedEmail();
        ExpenseObjective objective = service.createExpenseObjective(request, email);
        return ResponseEntity.ok(Map.of("message", "ExpenseObjective created successfully", "id", objective.getId().toString()));
    }

    @GetMapping("/{id}")
    public ExpenseObjective getExpenseObjectiveById(@PathVariable Long id) {
        return service.getExpenseObjectiveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateExpenseObjective(@PathVariable Long id, @RequestBody ExpenseObjectiveUpdateRequest request) {
        service.updateExpenseObjectiveById(id, request);
        return ResponseEntity.ok(Map.of("message", "ExpenseObjective updated successfully"));
    }

    @PutMapping("/update/{id}/{spentAmount}")
    public ResponseEntity<Map<String, String>> updateExpenseObjectiveOnlySpentAmount(@PathVariable Long id, @PathVariable int spentAmount) {
        service.updateExpenseObjectiveOnlySpentAmount(id, spentAmount);
        return ResponseEntity.ok(Map.of("message", "ExpenseObjective updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteExpenseObjective(@PathVariable Long id) {
        service.deleteExpenseObjectiveById(id);
        return ResponseEntity.ok(Map.of("message", "ExpenseObjective deleted successfully"));
    }

    @GetMapping("/list/{yearMonth}")
    public ResponseEntity<Map<String, List<ExpenseObjective>>> getExpenseObjectivesByYearAndMonth(
            @PathVariable @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String yearMonth
    ) {
        String email = getAuthenticatedEmail();
        List<ExpenseObjective> listOfObjectives = service.getExpenseObjectivesByYearMonth(email, yearMonth);
        return ResponseEntity.ok().body(Map.of("list", listOfObjectives));
    }


    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
