package com.gdgyonsei.otp.domains.expenseobjective.service;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.repository.ExpenseObjectiveRepository;
import com.gdgyonsei.otp.domains.spending.service.ExpenseObjectiveHelperService;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseObjectiveService {
    private final ExpenseObjectiveRepository repository;
    private final BadgeOwnershipService badgeOwnershipService;
    private final BadgeService badgeService;
    private final ExpenseObjectiveHelperService spendingService;

    @Transactional
    public ExpenseObjective createExpenseObjective(ExpenseObjectiveCreateRequest request, String memberEmail) {
        // Get previously spent amount of money in this month
        String targetMonth = request.getTargetMonth();
        if (!targetMonth.matches("\\d{4}-\\d{2}")) {
            throw new IllegalArgumentException("Invalid targetMonth format: " + targetMonth);
        }
        int year = Integer.parseInt(targetMonth.substring(0, 4));
        int month = Integer.parseInt(targetMonth.substring(5, 7));
        UpperCategoryType category;
        try {
            category = UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UpperCategoryType: " + request.getUpperCategoryType(), e);
        }
        int spentAmount = spendingService.getTotalExpenseByYearAndMonthAndCategory(memberEmail, year, month, category);

        // Generate an ExpenseObjective
        ExpenseObjective objective = new ExpenseObjective();
        objective.setUpperCategoryType(category);
        objective.setExpenseLimit(request.getExpenseLimit());
        objective.setSpentAmount(spentAmount);
        objective.setMemberEmail(memberEmail);
        objective.setTargetMonth(targetMonth);

        // Generate Badge if necessary (atomic operation)
        synchronized (this) {
            if (!badgeOwnershipService.getFirstExpenseObjectiveAdd(memberEmail)) {
                badgeOwnershipService.updateFirstExpenseObjectiveAdd(memberEmail, true);
                badgeService.createBadge(memberEmail, "FIRST_EXPENSE_OBJECTIVE_ADD", null, null);
            }
        }

        return repository.save(objective);
    }

    @Transactional(readOnly = true)
    public ExpenseObjective getExpenseObjectiveById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpenseObjective not found with id " + id));
    }

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectiveListByEmail(String email) {
        return repository.findAllByMemberEmailOrderByIdAsc(email);
    }

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectiveListByEmailAndTargetMonth(
            String email, @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String yearMonth) {
        return repository.findAllByMemberEmailAndTargetMonthOrderByIdAsc(email, yearMonth);
    }

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectiveListByEmailAndTargetMonthAndUpperCategoryType(
            String email, @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String targetMonth,
            UpperCategoryType upperCategoryType) {
        return repository.findAllByMemberEmailAndTargetMonthAndUpperCategoryTypeOrderByIdAsc(email, targetMonth, upperCategoryType);
    }

    @Transactional
    public void updateExpenseObjectiveById(Long id, ExpenseObjectiveUpdateRequest request) {
        ExpenseObjective objective = getExpenseObjectiveById(id);
        objective.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        objective.setExpenseLimit(request.getExpenseLimit());
        objective.setSpentAmount(request.getSpentAmount());
        objective.setTargetMonth(request.getTargetMonth());
        repository.save(objective);
    }

    @Transactional
    public void updateExpenseObjectiveOnlySpentAmount(Long id, int spentAmount) {
        ExpenseObjective objective = getExpenseObjectiveById(id);
        objective.setSpentAmount(spentAmount);
        repository.save(objective);
    }

    @Transactional
    public void updateExpenseObjectiveOnlyExpenseLimit(Long id, int expenseLimit) {
        ExpenseObjective objective = getExpenseObjectiveById(id);
        objective.setExpenseLimit(expenseLimit);
        // repository.save(objective);
    }

    @Transactional
    public void deleteExpenseObjectiveById(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void deleteExpenseObjectivesByEmail(String email) {
        repository.deleteByMemberEmail(email);
    }

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectivesByYearMonth(String memberEmail, String yearMonth) {
        return repository.findAllByYearAndMonth(memberEmail, yearMonth);
    }
}

