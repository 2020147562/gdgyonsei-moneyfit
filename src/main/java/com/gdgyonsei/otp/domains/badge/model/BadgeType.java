package com.gdgyonsei.otp.domains.badge.model;

public enum BadgeType {
    SHARED_TO_THREE_FRIENDS,
    SIGN_UP_COMPLETE,
    FIRST_EXPENSE_OBJECTIVE_ADD,
    FIRST_EXPENSE_OBJECTIVE_ACHIEVE,
    FIRST_RANDOM_BOX_TRIAL,
    FIRST_RANDOM_BOX_SUCCESS,
    CONSECUTIVE_EXPENSE_OBJECTIVE_ACHIEVE,          // 추가적인 필드로 몇 개월 연속인지 기술
    SPECIFIC_MONTH_EXPENSE_OBJECTIVE_ACHIEVE        // 추가적인 필드로 몇 년도 몇 월의 달성인지 기술
}
