package com.gdgyonsei.otp.domains.point.service;

import com.gdgyonsei.otp.domains.badge.service.BadgeOwnershipService;
import com.gdgyonsei.otp.domains.badge.service.BadgeService;
import com.gdgyonsei.otp.domains.point.model.Points;
import com.gdgyonsei.otp.domains.point.repository.PointsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static com.gdgyonsei.otp.domains.point.constants.Constants.BASIC_ATTENDANCE_POINTS;
import static com.gdgyonsei.otp.domains.point.constants.Constants.GATCHA_COST;

@RequiredArgsConstructor
@Service
public class PointsService {

    private final JavaMailSender mailSender;
    private final PointsRepository pointsRepository;
    private final BadgeService badgeService;
    private final BadgeOwnershipService badgeOwnershipService;

    @Transactional
    public Points createPoints(String memberEamil) {
        Points points = new Points();
        points.setMemberEmail(memberEamil);
        points.setLeftPoints(0L);
        points.setLastAccessDate(LocalDate.now().minusDays(2));    // Not to make consecutive access
        points.setConsecutiveDays(0);                                           // zero means this is just created.
        return pointsRepository.save(points);
    }

    @Transactional(readOnly = true)
    public Points getPointsByEmail(String email) {
        return pointsRepository.findByMemberEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Points not found for memberEmail: " + email));
    }

    @Transactional
    public boolean gatcha10WithEmail(String memberEmail, String giftyEmail) {
        Points points = getPointsByEmail(memberEmail);
        // If remaining points are under 1000, immediately fail the gatcha.
        if (points.getLeftPoints() < GATCHA_COST) {
            return false;
        }
        // Cost Deduction
        points.setLeftPoints(points.getLeftPoints() - GATCHA_COST);

        if (!badgeOwnershipService.getFirstRandomBoxTrial(memberEmail)) {
            badgeOwnershipService.updateFirstRandomBoxTrial(memberEmail, true);
            String badgeTypeString = "FIRST_RANDOM_BOX_TRIAL";
            badgeService.createBadge(memberEmail, badgeTypeString, null, null);
        }
        boolean isSuccess = new Random().nextInt(100) < 10;
        if (isSuccess) {
            sendSuccessEmail(giftyEmail);
            if (!badgeOwnershipService.getFirstRandomBoxSuccess(memberEmail)) {
                badgeOwnershipService.updateFirstRandomBoxSuccess(memberEmail, true);
                String badgeTypeString = "FIRST_RANDOM_BOX_SUCCESS";
                badgeService.createBadge(memberEmail, badgeTypeString, null, null);
            }
        }
        return isSuccess;
    }

    // return : List(consecutiveDays, leftPoints)
    @Transactional
    public List<Object> provideAttendancePoints(String email) {
        Points points = getPointsByEmail(email);
        LocalDate today = LocalDate.now();
        LocalDate lastAccessDate = points.getLastAccessDate();

        // If the points are already provided to the member, just return current point status
        if (today.equals(lastAccessDate)) {
            return List.of(points.getConsecutiveDays(), points.getLeftPoints());
        }

        if (lastAccessDate.equals(today.minusDays(1))) {
            // Case of consecutive attendance
            int consecutiveDays = points.getConsecutiveDays();
            if (consecutiveDays >= 5) {
                points.setConsecutiveDays(1);
            } else {
                points.setConsecutiveDays(consecutiveDays + 1);
            }
            int bonusPoints = (points.getConsecutiveDays() - 1) * 10;
            points.setLeftPoints(points.getLeftPoints() + BASIC_ATTENDANCE_POINTS + bonusPoints);
        } else if (lastAccessDate.isBefore(today.minusDays(1))) {
            // Case of non-consecutive attendance
            points.setConsecutiveDays(1);
            points.setLeftPoints(points.getLeftPoints() + BASIC_ATTENDANCE_POINTS);
        } else if (lastAccessDate.isAfter(today)) {
            // Case which should not happen
            throw new IllegalArgumentException("Invalid lastAccessDate. Future dates are not allowed.");
        }

        // Attendance process completed
        points.setLastAccessDate(today);
        pointsRepository.save(points);

        return List.of(points.getConsecutiveDays(), points.getLeftPoints());
    }

    @Transactional
    public Long addThousandPointsWithEmail(String email) {
        Points points = getPointsByEmail(email);
        Long result = points.getLeftPoints() + 1000L;
        points.setLeftPoints(result);
        pointsRepository.save(points);
        return result;
    }

    @Transactional
    public void deletePointsByEmail(String email) {
        pointsRepository.deleteByMemberEmail(email);
    }

    private void sendSuccessEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Moneyfit Gatcha Success Notification!!!");
        message.setText("Congratulations! You succeeded in the 10% gatcha!");
        mailSender.send(message);
        System.out.println("Success email sent to: " + email);
    }

}

