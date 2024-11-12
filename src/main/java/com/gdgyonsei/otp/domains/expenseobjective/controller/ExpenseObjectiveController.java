package com.gdgyonsei.otp.domains.expenseobjective.controller;

import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.service.ExpenseObjectiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/android/expense-objective")
public class ExpenseObjectiveController {
    private final ExpenseObjectiveService service;

    public ExpenseObjectiveController(ExpenseObjectiveService service) {
        this.service = service;
    }

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteExpenseObjective(@PathVariable Long id) {
        service.deleteExpenseObjectiveById(id);
        return ResponseEntity.ok(Map.of("message", "ExpenseObjective deleted successfully"));
    }

    @GetMapping("/list")
    public List<ExpenseObjective> getAllExpenseObjectives() {
        String email = getAuthenticatedEmail();
        return service.getAllByMemberEmail(email);
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
