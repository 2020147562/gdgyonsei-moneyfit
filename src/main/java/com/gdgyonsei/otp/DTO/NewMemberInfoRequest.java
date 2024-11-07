package com.gdgyonsei.otp.DTO;

// DTO 클래스
public class NewMemberInfoRequest {
    private String name;
    private String gender;
    private String incomeLevel;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getIncomeLevel() { return incomeLevel; }
    public void setIncomeLevel(String incomeLevel) { this.incomeLevel = incomeLevel; }
}
