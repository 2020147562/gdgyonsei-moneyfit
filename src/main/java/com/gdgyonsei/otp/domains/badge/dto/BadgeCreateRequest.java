package com.gdgyonsei.otp.domains.badge.dto;

import com.gdgyonsei.otp.domains.badge.model.BadgeType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BadgeCreateRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private BadgeType badgeType;

    private Integer consecutiveMonths;  // Optional, 기본값 처리 가능

    @Pattern(regexp = "\\d{4}-\\d{2}", message = "specificYearMonth must be in the format yyyy-MM")
    private String specificYearMonth;  // Optional, 기본값 처리 가능
}
