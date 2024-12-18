package com.gdgyonsei.otp.domains.point.controller;

import com.gdgyonsei.otp.domains.point.model.Points;
import com.gdgyonsei.otp.domains.point.service.PointsService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/android/points")
public class PointsController {
    private final PointsService pointsService;

    @GetMapping
    public ResponseEntity<Map<String, Long>> getPointsByMemberEmail() {
        String email = getAuthenticatedEmail();

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("leftPoints", pointsService.getPointsByEmail(email).getLeftPoints()));
    }

    @PutMapping("/gatcha10/{giftyEmail}")
    public ResponseEntity<Map<String, Boolean>> gatcha10(
            @PathVariable
            @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                    message = "Invalid email format")
            String giftyEmail
    ) {
        String memberEmail = getAuthenticatedEmail();
        boolean isSuccess = pointsService.gatcha10WithEmail(memberEmail, giftyEmail);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("isSuccess", isSuccess));
    }

    @PutMapping("/attendancePoints")
    public ResponseEntity<Map<String, ?>> payAttendancePoints() {
        String email = getAuthenticatedEmail();
        List<Object> results = pointsService.provideAttendancePoints(email);
        int consecutiveDays = (int) results.get(0);
        long leftPoints = (long) results.get(1);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("consecutiveDays", consecutiveDays, "leftPoints", leftPoints));
    }

    @PutMapping("/addThousandPoints")
    public ResponseEntity<Map<String, Long>> addThousandPoints() {
        String email = getAuthenticatedEmail();
        Long leftPoints = pointsService.addThousandPointsWithEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("leftPoints", leftPoints));
    }

    @GetMapping("/doesGetPointsToday")
    public ResponseEntity<Map<String, ?>> doesGetPointsToday() {
        String email = getAuthenticatedEmail();
        List<Object> results = pointsService.doesGetPointsToday(email);
        boolean doesGetPointsToday = (boolean) results.get(0);
        int consecutiveDays = (int) results.get(1);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("doesGetPointsToday", doesGetPointsToday, "consecutiveDaysBeforeGetAttendancePoints", consecutiveDays));
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
