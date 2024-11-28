package com.gdgyonsei.otp.domains.expenseobjective.repository;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExpenseObjectiveRepository extends JpaRepository<ExpenseObjective, Long> {
    @Query("SELECT e FROM ExpenseObjective e WHERE e.memberEmail = :memberEmail AND e.targetMonth = :yearMonth")
    List<ExpenseObjective> findAllByYearAndMonth(@Param("memberEmail") String memberEmail, @Param("yearMonth") String yearMonth);
}

