package com.gdgyonsei.otp.domains.badge.model;

public enum BadgeType {
    SHARED_TO_THREE_FRIENDS,
    SIGN_UP_COMPLETE,                               // Member 생성 시 자동 생성
    FIRST_EXPENSE_OBJECTIVE_ADD,                    // ExpenseObjective 생성 시 자동 생성
    FIRST_EXPENSE_OBJECTIVE_ACHIEVE,
    FIRST_RANDOM_BOX_TRIAL,                         // Gatcha 시도 시 자동 생성
    FIRST_RANDOM_BOX_SUCCESS,                       // Gatcha 성공 시 자동 생성
    CONSECUTIVE_EXPENSE_OBJECTIVE_ACHIEVE,          // 추가적인 필드로 몇 개월 연속인지 기술
    SPECIFIC_MONTH_EXPENSE_OBJECTIVE_ACHIEVE        // 추가적인 필드로 몇 년도 몇 월의 달성인지 기술
}
