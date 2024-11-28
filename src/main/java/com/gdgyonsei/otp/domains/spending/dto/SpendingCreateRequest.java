package com.gdgyonsei.otp.domains.spending.dto;

import lombok.Data;

@Data
public class SpendingCreateRequest {
    private String upperCategoryType;
    private String dateTime;
    private int expenseAmount;
}

