package com.gdgyonsei.otp.domains.spending.repository;

import com.gdgyonsei.otp.domains.spending.model.Spending;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {
    List<Spending> findAllByMemberEmail(String memberEmail);
}
