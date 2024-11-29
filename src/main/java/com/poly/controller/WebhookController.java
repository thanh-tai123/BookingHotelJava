package com.poly.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.service.BookingService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private final BookingService bookingService;

    @Value("${CASSO_API_KEY}")
    private String cassoApiKey;

    public WebhookController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/payment-success")
    public void handlePaymentSuccess(
            @RequestBody Map<String, Object> payload,
            @RequestHeader("X-Casso-Signature") String signature,
            HttpServletResponse response) throws JsonProcessingException, IOException {

        // Xác thực chữ ký của yêu cầu
        if (!isValidSignature(payload, signature)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid signature");
            return;
        }

        // Xử lý sự kiện thanh toán thành công
        try {
            // Lấy thông tin cần thiết từ payload
            String orderId = (String) payload.get("order_id");
            String status = (String) payload.get("status");

            if ("paid".equals(status)) {
                // Cập nhật trạng thái thanh toán trong hệ thống của bạn
                // bookingService.updatePaymentStatus(orderId, "Paid");
                System.out.println("Ok");
            }

            // Chuyển hướng về trang home.html
            response.sendRedirect("/");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing payment success");
        }
    }

    private boolean isValidSignature(Map<String, Object> payload, String signature) throws JsonProcessingException {
        // Implement logic to validate signature using Casso API key
        // This is an example implementation, you need to adapt it to your specific case
        String payloadString = new ObjectMapper().writeValueAsString(payload);
        String computedSignature = computeHmacSHA256(payloadString, cassoApiKey);
        return computedSignature.equals(signature);
    }

    private String computeHmacSHA256(String data, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate HMAC SHA-256", e);
        }
    }
}
