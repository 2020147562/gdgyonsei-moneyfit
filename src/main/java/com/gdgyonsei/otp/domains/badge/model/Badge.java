package com.gdgyonsei.otp.domains.badge.model;

import com.gdgyonsei.otp.domains.badge.validation.ValidBadge;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ValidBadge
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BadgeType badgeType;

    // only needed when badgeType = CONSECUTIVE_EXPENSE_OBJECTIVE_ACHIEVE
    @Column
    private int consecutiveMonths;

    // only needed when badgeType = SPECIFIC_MONTH_EXPENSE_OBJECTIVE_ACHIEVE
    @Column
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "specificYearMonth must be in the format yyyy-MM")
    private String specificYearMonth;

    @Column(nullable = false)
    private LocalDateTime acquiredDateTime;

}
