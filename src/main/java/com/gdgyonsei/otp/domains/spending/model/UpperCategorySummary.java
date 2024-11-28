package com.gdgyonsei.otp.domains.spending.model;

import com.gdgyonsei.otp.domains.util.UpperCategoryType;

public interface UpperCategorySummary {
    UpperCategoryType getCategory();
    int getTotalExpense();
}
