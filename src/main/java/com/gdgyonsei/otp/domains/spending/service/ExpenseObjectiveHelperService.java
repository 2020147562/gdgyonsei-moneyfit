package com.gdgyonsei.otp.domains.spending.service;

import com.gdgyonsei.otp.domains.spending.repository.SpendingRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseObjectiveHelperService {
    private final SpendingRepository spendingRepository;

    @Transactional(readOnly = true)
    public int getTotalExpenseByYearAndMonthAndCategory(String memberEmail, int year, int month, UpperCategoryType upperCategoryType) {
        return spendingRepository.findTotalExpenseByYearAndMonthAndCategory(memberEmail, year, month, upperCategoryType.toString());
    }

}
