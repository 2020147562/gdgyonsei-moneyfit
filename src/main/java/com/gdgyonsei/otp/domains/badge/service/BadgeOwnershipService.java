package com.gdgyonsei.otp.domains.badge.service;

import com.gdgyonsei.otp.domains.badge.model.BadgeOwnership;
import com.gdgyonsei.otp.domains.badge.repository.BadgeOwnershipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BadgeOwnershipService {

    private final BadgeOwnershipRepository badgeOwnershipRepository;

    @Transactional
    public void createBadgeOwnershipService(String memberEmail) {
        BadgeOwnership badgeOwnership = BadgeOwnership.builder()
                .memberEmail(memberEmail)
                .sharedToThreeFriends(false)
                .signUpComplete(true)
                .firstExpenseObjectiveAdd(false)
                .firstExpenseObjectiveAchieve(false)
                .firstRandomBoxTrial(false)
                .firstRandomBoxSuccess(false)
                .build();
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional(readOnly = true)
    public boolean getSharedToThreeFriends(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isSharedToThreeFriends();
    }

    @Transactional(readOnly = true)
    public boolean getSignUpComplete(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isSignUpComplete();
    }

    @Transactional(readOnly = true)
    public boolean getFirstExpenseObjectiveAdd(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isFirstExpenseObjectiveAdd();
    }

    @Transactional(readOnly = true)
    public boolean getFirstExpenseObjectiveAchieve(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isFirstExpenseObjectiveAchieve();
    }

    @Transactional(readOnly = true)
    public boolean getFirstRandomBoxTrial(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isFirstRandomBoxTrial();
    }

    @Transactional(readOnly = true)
    public boolean getFirstRandomBoxSuccess(String memberEmail) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        return badgeOwnership.isFirstRandomBoxSuccess();
    }

    @Transactional
    public void updateSharedToThreeFriends(String memberEmail, boolean value) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        badgeOwnership.setSharedToThreeFriends(value);
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional
    public void updateFirstExpenseObjectiveAdd(String memberEmail, boolean value) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        badgeOwnership.setFirstExpenseObjectiveAdd(value);
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional
    public void updateFirstExpenseObjectiveAchieve(String memberEmail, boolean value) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        badgeOwnership.setFirstExpenseObjectiveAchieve(value);
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional
    public void updateFirstRandomBoxTrial(String memberEmail, boolean value) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        badgeOwnership.setFirstRandomBoxTrial(value);
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional
    public void updateFirstRandomBoxSuccess(String memberEmail, boolean value) {
        BadgeOwnership badgeOwnership = getBadgeOwnershipByEmail(memberEmail);
        badgeOwnership.setFirstRandomBoxSuccess(value);
        badgeOwnershipRepository.save(badgeOwnership);
    }

    @Transactional
    public void deleteBadgeOwnershipByEmail(String email) {
        badgeOwnershipRepository.deleteByMemberEmail(email);
    }
    private BadgeOwnership getBadgeOwnershipByEmail(String memberEmail) {
        return badgeOwnershipRepository.findByMemberEmail(memberEmail);
    }

}
