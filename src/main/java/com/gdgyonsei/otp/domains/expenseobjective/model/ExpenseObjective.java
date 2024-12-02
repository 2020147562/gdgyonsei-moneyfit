package com.gdgyonsei.otp.domains.expenseobjective.model;

import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.YearMonth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ExpenseObjective {
    // ㅇㅇ 카테고리의 지출 ㅁㅁ원으로 제한하기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category
    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // Enum 저장 시 문자열로 저장
    private UpperCategoryType upperCategoryType;

    // Maximum spending
    @Column(nullable = false)
    private int expenseLimit;

    // Spent amount out of expenseLimit
    @Column(nullable = false)
    private int spentAmount;

    // Member email who own this objective
    @Column(nullable = false)
    private String memberEmail;

    // target month of this objective
    @Column(nullable = false)
    @Pattern(regexp = "\\d{4}-\\d{2}", message = "Invalid yearMonth format. Expected yyyy-MM.")
    private String targetMonth;
}

