package com.gdgyonsei.otp.domains.expenseobjective.repository;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseObjectiveRepository extends JpaRepository<ExpenseObjective, Long> {
    List<ExpenseObjective> findAllByMemberEmail(String memberEmail);
}

