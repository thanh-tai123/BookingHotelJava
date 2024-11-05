package com.poly.service;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.poly.dto.BookingRequest;
import com.poly.dto.PaymentRequest;
import com.poly.rescontroller.PayosConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class PaymentService {

//    @Autowired
//    private PayosConfig payosConfig;
//
//    @Autowired
//    private PayosConfig payOSConfig;
//
//    private final RestTemplate restTemplate;
//
//    // Constructor để khởi tạo RestTemplate
//    public PaymentService() {
//        this.restTemplate = new RestTemplate();
//    }
//
//    public String initiatePayment(PaymentRequest paymentRequest) {
//        String url = payOSConfig.getEndpointUrl();
//
//        // Thiết lập header
//        HttpHeaders headers = new HttpHeaders(null);
//        headers.set("Content-Type", "application/json");
//        headers.set("API-Key", payOSConfig.getApiKey());
//
//        // Tạo yêu cầu
//        HttpEntity<PaymentRequest> requestEntity = new HttpEntity<>(paymentRequest, headers);
//
//        try {
//            // Gửi yêu cầu đến PayOS
//            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//            return responseEntity.getBody();
//        } catch (RestClientException e) {
//            // Xử lý lỗi khi gửi yêu cầu
//            throw new RuntimeException("Payment initiation failed: " + e.getMessage(), e);
//        }
//    }

}