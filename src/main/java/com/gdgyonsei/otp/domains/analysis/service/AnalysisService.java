package com.gdgyonsei.otp.domains.analysis.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalysisService {

    @Value("${flask.server.url}")
    private String flaskServerUrl;  // Flask 서버 URL (application.properties에 설정)

    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public boolean updateModel(String itemId, String userEmail) {
        // Flask 엔드포인트 URL
        String url = flaskServerUrl + "/update_model";

        // 요청 데이터 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("item_id", itemId);
        requestBody.put("user_email", userEmail);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 엔티티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Flask 엔드포인트에 POST 요청
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            // 응답 처리
            if (response.getStatusCode().is2xxSuccessful()) {
                return true;
            } else {
                throw new RuntimeException("Flask Server Error " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Flask 서버에 요청을 보내는 도중 오류가 발생했습니다.", e);
        }
    }

    @Transactional
    public boolean checkOverConsumption(String itemId, String userEmail) {
        // Flask 엔드포인트 URL
        String url = flaskServerUrl + "/is_over_consumption";

        // 요청 데이터 구성
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("item_id", itemId);
        requestBody.put("user_email", userEmail);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 엔티티 생성
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Flask 엔드포인트에 POST 요청
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            // 응답 처리

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return (boolean) response.getBody().get("is_over_consumption");
            } else {
                throw new RuntimeException("Flask Server Error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Flask 서버에 요청을 보내는 도중 오류가 발생했습니다.", e);
        }
    }
}

