package com.gdgyonsei.otp.domains.spending.service;

import com.gdgyonsei.otp.domains.spending.dto.SpendingCreateRequest;
import com.gdgyonsei.otp.domains.spending.dto.SpendingUpdateRequest;
import com.gdgyonsei.otp.domains.spending.model.Spending;
import com.gdgyonsei.otp.domains.spending.repository.SpendingRepository;
import com.gdgyonsei.otp.domains.util.UpperCategoryType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        spending.setDate(LocalDate.parse(request.getDate()));
        spending.setExpenseAmount(request.getExpenseAmount());
        return spendingRepository.save(spending); // 저장된 객체 반환
    }

    @Transactional
    public Spending getSpendingById(Long id) {
        return spendingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Spending not found with id " + id));
    }

    @Transactional
    public void updateSpendingById(Long id, SpendingUpdateRequest request) {
        Spending spending = getSpendingById(id);
        spending.setUpperCategoryType(UpperCategoryType.valueOf(request.getUpperCategoryType().toUpperCase()));
        spending.setDate(LocalDate.parse(request.getDate()));
        spending.setExpenseAmount(request.getExpenseAmount());
        spendingRepository.save(spending);
    }

    @Transactional
    public void deleteSpendingById(Long id) {
        spendingRepository.deleteById(id);
    }

    public List<Spending> getSpendingListByMemberEmail(String memberEmail) {
        return spendingRepository.findAllByMemberEmail(memberEmail);
    }
}


