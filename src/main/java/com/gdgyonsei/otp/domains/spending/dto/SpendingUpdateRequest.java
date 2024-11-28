package com.gdgyonsei.otp.domains.spending.dto;

import lombok.Data;

@Data
public class SpendingUpdateRequest {
    private String upperCategoryType;
    private String dateTime;
    private int expenseAmount;
}
