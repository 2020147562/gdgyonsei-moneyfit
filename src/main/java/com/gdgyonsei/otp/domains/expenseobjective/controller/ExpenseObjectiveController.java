package com.gdgyonsei.otp.domains.expenseobjective.controller;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.service.ExpenseObjectiveService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
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

    @GetMapping("/get")
    public ResponseEntity<Map<String, List<ExpenseObjective>>> getExpenseObjectiveList() {
        String email = getAuthenticatedEmail();
        List<ExpenseObjective> list = service.getExpenseObjectiveListByEmail(email);
        return ResponseEntity.ok(Map.of("expenseObjective list for this member", list));
    }

    @GetMapping("/get/{targetMonth}")
    public ResponseEntity<Map<String, List<ExpenseObjective>>> getExpenseObjectiveListByEmailAndTargetMonth(
            @PathVariable @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String targetMonth) {
        String email = getAuthenticatedEmail();
        List<ExpenseObjective> list = service.getExpenseObjectiveListByEmailAndTargetrMonth(email, targetMonth);
        return ResponseEntity.ok(Map.of("expenseObjective list for this member", list));
    }

    @GetMapping("/get/{targetMonth}/{upperCategoryType}")
    public ResponseEntity<Map<String, List<ExpenseObjective>>> getExpenseObjectiveListByMonthAndUpperCategoryType(
            @PathVariable @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String targetMonth,
            String upperCategoryType
    ) {
        String email = getAuthenticatedEmail();
        List<ExpenseObjective> list =
                service.getExpenseObjectiveListByEmailAndTargetMonthAndUpperCategoryType(email, targetMonth, UpperCategoryType.valueOf(upperCategoryType.toUpperCase()));
        return ResponseEntity.ok(Map.of("expenseObjective list for this member", list));
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

    @PutMapping("/update/{id}/{expenseLimit}")
    public ResponseEntity<Map<String, String>> updateExpenseObjectiveOnlyExpenseLimit(@PathVariable Long id, @PathVariable int expenseLimit) {
        service.updateExpenseObjectiveOnlyExpenseLimit(id, expenseLimit);
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
