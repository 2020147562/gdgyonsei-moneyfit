package com.gdgyonsei.otp.domains.expenseobjective.service;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.expenseobjective.repository.ExpenseObjectiveRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpendingHelperService {
    private final ExpenseObjectiveRepository expenseObjectiveRepository;

    @Transactional(readOnly = true)
    public List<ExpenseObjective> getExpenseObjectiveListByEmailAndTargetMonthAndUpperCategoryType(
            String email, @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.") String targetMonth,
            UpperCategoryType upperCategoryType) {
        return expenseObjectiveRepository.findAllByMemberEmailAndTargetMonthAndUpperCategoryTypeOrderByIdAsc(email, targetMonth, upperCategoryType);
    }
}
