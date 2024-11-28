package com.gdgyonsei.otp.domains.point.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private Long leftPoints;

    // it's for attendance point management
    @Column(nullable = false)
    private LocalDate lastAccessDate;

    // Attendance points provided differs by this field
    @Column(nullable = false)
    private int consecutiveDays;
}
