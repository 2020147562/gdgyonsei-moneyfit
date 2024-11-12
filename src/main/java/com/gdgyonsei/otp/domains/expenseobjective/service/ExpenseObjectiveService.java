package com.gdgyonsei.otp.domains.expenseobjective.service;

import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveCreateRequest;
import com.gdgyonsei.otp.domains.expenseobjective.dto.ExpenseObjectiveUpdateRequest;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.repository.ExpenseObjectiveRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseObjectiveService {
    private final ExpenseObjectiveRepository repository;

    public ExpenseObjectiveService(ExpenseObjectiveRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ExpenseObjective createExpenseObjective(ExpenseObjectiveCreateRequest request, String memberEmail) {
        ExpenseObjective objective = new ExpenseObjective();
        objective.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        objective.setExpenseLimit(request.getExpenseLimit());
        objective.setSpentAmount(0); // 처음에는 0으로 설정
        objective.setMemberEmail(memberEmail);
        objective.setWhichMonth(request.getWhichMonth());
        return repository.save(objective);
    }

    @Transactional
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
        objective.setWhichMonth(request.getWhichMonth());
        repository.save(objective);
    }

    @Transactional
    public void deleteExpenseObjectiveById(Long id) {
        repository.deleteById(id);
    }

    public List<ExpenseObjective> getAllByMemberEmail(String memberEmail) {
        return repository.findAllByMemberEmail(memberEmail);
    }
}

