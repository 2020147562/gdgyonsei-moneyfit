package com.gdgyonsei.otp.domains.expenseobjective.repository;

import com.gdgyonsei.otp.domains.expenseobjective.model.ExpenseObjective;
import com.gdgyonsei.otp.domains.point.model.Points;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseObjectiveRepository extends JpaRepository<ExpenseObjective, Long> {
    List<ExpenseObjective> findAllByMemberEmail(String memberEmail);
    List<ExpenseObjective> findAllByMemberEmailAndTargetMonth(String memberEmail, String targetMonth);
    List<ExpenseObjective> findAllByMemberEmailAndTargetMonthAndUpperCategoryType
            (String memberEmail, String targetMonth, UpperCategoryType upperCategoryType);

    @Query("SELECT e FROM ExpenseObjective e WHERE e.memberEmail = :memberEmail AND e.targetMonth = :yearMonth")
    List<ExpenseObjective> findAllByYearAndMonth(@Param("memberEmail") String memberEmail, @Param("yearMonth") String yearMonth);
}

