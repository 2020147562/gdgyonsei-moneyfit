package com.gdgyonsei.otp.domains.expenseobjective.dto;

import lombok.Data;

@Data
public class ExpenseObjectiveCreateRequest {
    private String upperCategoryType;
    private int expenseLimit;
    private String whichMonth;
}

