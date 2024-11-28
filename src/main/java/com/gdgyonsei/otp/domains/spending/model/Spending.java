package com.gdgyonsei.otp.domains.spending.model;

import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Spending {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING) // UpperCategoryType은 Enum이므로 필요
    private UpperCategoryType upperCategoryType;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private int expenseAmount;
}

