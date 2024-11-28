package com.gdgyonsei.otp.domains.badge.model;

import com.gdgyonsei.otp.domains.badge.validation.ValidBadge;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ValidBadge
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BadgeOwnership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String memberEmail;

    // false means that this member doesn't have that kind of badge.
    @Column(nullable = false)
    private boolean sharedToThreeFriends;

    @Column(nullable = false)
    private boolean signUpComplete;

    @Column(nullable = false)
    private boolean firstExpenseObjectiveAdd;

    @Column(nullable = false)
    private boolean firstExpenseObjectiveAchieve;

    @Column(nullable = false)
    private boolean firstRandomBoxTrial;

    @Column(nullable = false)
    private boolean firstRandomBoxSuccess;
}
