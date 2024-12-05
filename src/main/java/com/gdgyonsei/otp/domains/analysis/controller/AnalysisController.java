package com.gdgyonsei.otp.domains.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.gdgyonsei.otp.domains.analysis.service.AnalysisService;

import java.util.Map;

@RestController
@RequestMapping("/android/analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @PostMapping("/updateModel")
    public ResponseEntity<Map<String, Boolean>> updateModel() {
        try {
            // AnalysisService의 updateModel 메서드 호출
            boolean isUpdateSuccess = analysisService.updateModel("1", getAuthenticatedEmail());
            // 응답 반환
            return ResponseEntity.ok(Map.of("isUpdateSuccess", isUpdateSuccess));
        } catch (Exception e) {
            // 오류 발생 시 500 상태 코드와 오류 메시지 반환
            return ResponseEntity.status(500).body(Map.of("isUpdateSuccess", false));
        }
    }

    private String getAuthenticatedEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}

