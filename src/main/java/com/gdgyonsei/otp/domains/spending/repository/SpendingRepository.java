package com.gdgyonsei.otp.domains.spending.repository;

import com.gdgyonsei.otp.domains.spending.model.Spending;
import com.gdgyonsei.otp.domains.spending.model.UpperCategorySummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpendingRepository extends JpaRepository<Spending, Long> {
    void deleteByMemberEmail(String memberEmail);

    @Query("SELECT s FROM Spending s WHERE s.memberEmail = :memberEmail ORDER BY s.dateTime DESC")
    List<Spending> findTopNSpendingByMemberEmail(@Param("memberEmail") String memberEmail, Pageable pageable);

    @Query("SELECT s.upperCategoryType AS category, SUM(s.expenseAmount) AS totalExpense " +
            "FROM Spending s WHERE s.memberEmail = :email AND YEAR(s.dateTime) = :year AND MONTH(s.dateTime) = :month GROUP BY s.upperCategoryType")
    List<UpperCategorySummary> findMonthlyExpenseByCategory(@Param("email") String memberEmail, @Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(s.expenseAmount) FROM Spending s WHERE s.memberEmail = :memberEmail AND YEAR(s.dateTime) = :year AND MONTH(s.dateTime) = :month")
    int findTotalExpenseByYearAndMonth(@Param("memberEmail") String memberEmail, @Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(s.expenseAmount) FROM Spending s WHERE s.memberEmail = :memberEmail AND YEAR(s.dateTime) = :year AND MONTH(s.dateTime) = :month AND s.upperCategoryType = :category")
    int findTotalExpenseByYearAndMonthAndCategory(@Param("memberEmail") String memberEmail, @Param("year") int year, @Param("month") int month, @Param("category") String category);
}
