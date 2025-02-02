package com.gdgyonsei.otp.model;

import jakarta.persistence.*;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    public enum Gender {
        MALE, FEMALE
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    public enum IncomeLevel {
        LOW, MEDIUM, HIGH
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IncomeLevel incomeLevel;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Gender getGender() { return gender; }
    public void setGender(Gender gender) { this.gender = gender; }
    public IncomeLevel getIncomeLevel() { return incomeLevel; }
    public void setIncomeLevel(IncomeLevel incomeLevel) { this.incomeLevel = incomeLevel; }

}
