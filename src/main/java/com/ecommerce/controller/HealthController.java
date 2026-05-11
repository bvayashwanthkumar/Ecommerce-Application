package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

/**
 * Health Controller - Application health check
 */
@RestController
@RequestMapping("")
public class HealthController {

    /**
     * GET / or /health - Main health check
     */
    @GetMapping({"/", "/health"})
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "✅ E-Commerce Application is healthy");
        response.put("timestamp", System.currentTimeMillis());
        response.put("version", "1.0.0");
        response.put("services", new String[]{
            "Products", "Users", "Orders"
        });
        return ResponseEntity.ok(response);
    }
}
