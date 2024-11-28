package com.gdgyonsei.otp.domains.expenseobjective.service;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.repository.ExpenseObjectiveRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseObjectiveService {
    private final ExpenseObjectiveRepository repository;
    private final BadgeOwnershipService badgeOwnershipService;
    private final BadgeService badgeService;

    @Transactional
    public ExpenseObjective createExpenseObjective(ExpenseObjectiveCreateRequest request, String memberEmail) {
        ExpenseObjective objective = new ExpenseObjective();
        objective.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        objective.setExpenseLimit(request.getExpenseLimit());
        objective.setSpentAmount(0); // 처음에는 0으로 설정
        objective.setMemberEmail(memberEmail);
        objective.setTargetMonth(YearMonth.parse(request.getTargetMonth()));

        if (!badgeOwnershipService.getFirstExpenseObjectiveAdd(memberEmail)) {
            badgeOwnershipService.updateFirstExpenseObjectiveAdd(memberEmail, true);
            String badgeTypeString = "FIRST_EXPENSE_OBJECTIVE_ADD";
            badgeService.createBadge(memberEmail, badgeTypeString, null, null);
        }

        return repository.save(objective);
    }

    @Transactional(readOnly = true)
    public ExpenseObjective getExpenseObjectiveById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpenseObjective not found with id " + id));
    }


    @Transactional
    public void updateExpenseObjectiveById(Long id, ExpenseObjectiveUpdateRequest request) {
        ExpenseObjective objective = getExpenseObjectiveById(id);
        objective.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        objective.setExpenseLimit(request.getExpenseLimit());
        objective.setSpentAmount(request.getSpentAmount());
        objective.setTargetMonth(YearMonth.parse(request.getTargetMonth()));
        repository.save(objective);
    }

    @Transactional
    public void updateExpenseObjectiveOnlySpentAmount(Long id, int spentAmount) {
        ExpenseObjective objective = getExpenseObjectiveById(id);
        objective.setSpentAmount(spentAmount);
        repository.save(objective);
    }

    @Transactional
    public void deleteExpenseObjectiveById(Long id) {
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectivesByYearMonth(String memberEmail, String yearMonth) {
        return repository.findAllByYearAndMonth(memberEmail, yearMonth);
    }
}

