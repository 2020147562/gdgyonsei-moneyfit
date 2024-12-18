package com.gdgyonsei.otp.domains.badge.validation;

import com.gdgyonsei.otp.domains.badge.dto.BadgeCreateRequest;
import com.gdgyonsei.otp.domains.badge.model.Badge;
import com.gdgyonsei.otp.domains.badge.validation.ValidBadge;
import com.gdgyonsei.otp.domains.badge.model.BadgeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BadgeValidator implements ConstraintValidator<ValidBadge, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BadgeType badgeType;
        Integer consecutiveMonths;
        String specificYearMonth;

        if (value instanceof Badge) {
            Badge badge = (Badge) value;
            badgeType = badge.getBadgeType();
            consecutiveMonths = badge.getConsecutiveMonths();
            specificYearMonth = badge.getSpecificYearMonth();
        } else if (value instanceof BadgeCreateRequest) {
            BadgeCreateRequest dto = (BadgeCreateRequest) value;
            badgeType = BadgeType.valueOf(dto.getBadgeTypeString());
            consecutiveMonths = dto.getConsecutiveMonths();
            specificYearMonth = dto.getSpecificYearMonth();
        } else {
            return true;  // 다른 객체는 검증하지 않음
        }

        if (badgeType == BadgeType.CONSECUTIVE_EXPENSE_OBJECTIVE_ACHIEVE) {
            if (consecutiveMonths == null || consecutiveMonths <= 0) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Consecutive months must be greater than 0")
                        .addPropertyNode("consecutiveMonths")
                        .addConstraintViolation();
                return false;
            }
        } else if (badgeType == BadgeType.SPECIFIC_MONTH_EXPENSE_OBJECTIVE_ACHIEVE) {
            if (specificYearMonth == null || !specificYearMonth.matches("\\d{4}-\\d{2}")) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Specific year month must match yyyy-MM")
                        .addPropertyNode("specificYearMonth")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }
}

