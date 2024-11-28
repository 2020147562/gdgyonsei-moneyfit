package com.gdgyonsei.otp.domains.expenseobjective.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ExpenseObjectiveCreateRequest {
    private String upperCategoryType;
    private int expenseLimit;
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "targetMonth must be in the format yyyy-MM")
    private String targetMonth;
}

