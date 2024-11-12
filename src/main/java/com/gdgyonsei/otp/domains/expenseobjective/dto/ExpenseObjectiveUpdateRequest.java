package com.gdgyonsei.otp.domains.expenseobjective.dto;

import lombok.Data;

@Data
public class ExpenseObjectiveUpdateRequest {
    private String upperCategoryType;
    private int expenseLimit;
    private int spentAmount;
    private String whichMonth;
}
