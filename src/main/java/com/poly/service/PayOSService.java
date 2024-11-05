package com.poly.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class PayOSService {

    private static final String PAYOS_API_URL = "https://api.payos.com/payment";
    private static final String PAYOS_API_KEY = "ec10c791-5279-491e-a4a7-5f5f121a650b";
    
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public PayOSService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String createPayment(double amount) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + PAYOS_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("amount", amount);
        requestBody.put("currency", "VND");
        requestBody.put("description", "Room booking payment");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(PAYOS_API_URL, request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Assuming the response contains the payment URL
            Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), Map.class);
            return responseBody.get("paymentUrl").toString();
        } else {
            throw new RuntimeException("Failed to create payment with PayOS: " + response.getBody());
        }
    }
}
