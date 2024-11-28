package com.gdgyonsei.otp.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DefaultErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        // Retrieve error status code
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        HttpStatus httpStatus = statusCode != null ? HttpStatus.resolve(statusCode) : HttpStatus.INTERNAL_SERVER_ERROR;

        // Retrieve error message and request URI
        String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");
        String requestUri = (String) request.getAttribute("jakarta.servlet.error.request_uri");

        // Build response body
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", httpStatus != null ? httpStatus.value() : 500);
        responseBody.put("error", httpStatus != null ? httpStatus.getReasonPhrase() : "Internal Server Error");
        responseBody.put("message", errorMessage != null ? errorMessage : "An unexpected error occurred");
        responseBody.put("path", requestUri != null ? requestUri : "N/A");

        // Return response entity
        return ResponseEntity
                .status(httpStatus != null ? httpStatus : HttpStatus.INTERNAL_SERVER_ERROR)
                .body(responseBody);
    }
}

