package com.gdgyonsei.otp.domains.spending.service;

import com.gdgyonsei.otp.domains.spending.dto.SpendingCreateRequest;
import com.gdgyonsei.otp.domains.spending.dto.SpendingUpdateRequest;
import com.gdgyonsei.otp.domains.spending.model.Spending;
import com.gdgyonsei.otp.domains.spending.model.UpperCategorySummary;
import com.gdgyonsei.otp.domains.spending.repository.SpendingRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SpendingService {
    private final SpendingRepository spendingRepository;

    public SpendingService(SpendingRepository spendingRepository) {
        this.spendingRepository = spendingRepository;
    }

    @Transactional
    public Spending createSpending(SpendingCreateRequest request, String memberEmail) {
        Spending spending = new Spending();
        spending.setMemberEmail(memberEmail);
        spending.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        spending.setDateTime(LocalDateTime.parse(request.getDateTime()));
        spending.setExpenseAmount(request.getExpenseAmount());
        return spendingRepository.save(spending); // 저장된 객체 반환
    }

    @Transactional(readOnly = true)
    public Spending getSpendingById(Long id) {
        return spendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spending not found with id " + id));
    }

    @Transactional
    public void updateSpendingById(Long id, SpendingUpdateRequest request) {
        Spending spending = getSpendingById(id);
        spending.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        spending.setDateTime(LocalDateTime.parse(request.getDateTime()));
        spending.setExpenseAmount(request.getExpenseAmount());
        spendingRepository.save(spending);
    }

    @Transactional
    public void deleteSpendingById(Long id) {
        spendingRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Spending> getTopNSpendings(String memberEmail, int n, int page) {
        Pageable pageable = PageRequest.of(page, n); // 첫 번째 패러미터 : 가져올 페이지, 두 번째 패러미터 : 한 페이지에 들어가는 entity 개수
        return spendingRepository.findTopNSpendingByMemberEmail(memberEmail, pageable);
    }

    // 특정 유저의 전체 지출을 카테고리별로 정렬해서 총합을 내림차순으로 반환
    @Transactional(readOnly = true)
    public List<UpperCategorySummary> getMonthlyExpenseByCategory(String memberEmail, int year, int month) {
        List<UpperCategorySummary> summaries = spendingRepository.findMonthlyExpenseByCategory(memberEmail, year, month);
        // 내림차순 정렬
        return summaries.stream()
                .sorted((a, b) -> Integer.compare(b.getTotalExpense(), a.getTotalExpense()))
                .toList();
    }

    // 특정 유저의 특정 연월의 총지출 반환
    @Transactional(readOnly = true)
    public Double getTotalExpenseByYearAndMonth(String memberEmail, int year, int month) {
        return spendingRepository.findTotalExpenseByYearAndMonth(memberEmail, year, month);
    }


}


