package com.poly.rescontroller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import vn.payos.PayOS;



@RestController
public class PaymentController {

//    @GetMapping("/pay")
//    public String getPay(@RequestParam("total") String amountStr) throws UnsupportedEncodingException {
//        HttpRequest req;
//        String vnp_Version = "2.1.0";
//        String vnp_Command = "pay";
//        String orderType = "other";
//        long amount;
//        try {
//            // Parse amount as a double
//            double amountDouble = Double.parseDouble(amountStr);
//            
//            // Convert double to long (considering it's in cents)
//            amount = Math.round(amountDouble * 100);
//        } catch (NumberFormatException e) {
//            return "Invalid amount format: " + e.getMessage();
//        }
//        String bankCode = "NCB";
//        
//        String vnp_TxnRef = Config.getRandomNumber(8);
//        String vnp_IpAddr = "127.0.0.1";
//
//        String vnp_TmnCode = Config.vnp_TmnCode;
//        
//        Map<String, String> vnp_Params = new HashMap<>();
//        vnp_Params.put("vnp_Version", vnp_Version);
//        vnp_Params.put("vnp_Command", vnp_Command);
//        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
//        vnp_Params.put("vnp_Amount", String.valueOf(amount));
//        vnp_Params.put("vnp_CurrCode", "VND");
//        
//        vnp_Params.put("vnp_BankCode", bankCode);
//        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
//        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
//        vnp_Params.put("vnp_OrderType", orderType);
//
//        vnp_Params.put("vnp_Locale", "vn");
//        vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
//        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
//
//        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
//        
//        cld.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(cld.getTime());
//        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
//        
//        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
//        Collections.sort(fieldNames);
//        StringBuilder hashData = new StringBuilder();
//        StringBuilder query = new StringBuilder();
//        Iterator<String> itr = fieldNames.iterator();
//        while (itr.hasNext()) {
//            String fieldName = itr.next();
//            String fieldValue = vnp_Params.get(fieldName);
//            if ((fieldValue != null) && (fieldValue.length() > 0)) {
//                //Build hash data
//                hashData.append(fieldName);
//                hashData.append('=');
//                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                //Build query
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
//                query.append('=');
//                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
//                if (itr.hasNext()) {
//                    query.append('&');
//                    hashData.append('&');
//                }
//            }
//        }
//        String queryUrl = query.toString();
//        String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
//        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
//        String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
//        
//        return "<html>" +
//               "<head>" +
//               "<meta http-equiv='refresh' content='0; url=" + paymentUrl + "' />" +
//               "</head>" +
//               "<body>" +
//               "Redirecting to payment..." +
//               "</body>" +
//               "</html>";
//    }
    private final PayOS payOS;

    @Autowired
    public PaymentController(PayOS payOS) {
        this.payOS = payOS;
    }
    
    @GetMapping("/pay")
    public ResponseEntity<?> getPay(@RequestParam("total") String amountStr) {
        long amount;
        try {
            double amountDouble = Double.parseDouble(amountStr);
            amount = Math.round(amountDouble * 100); // Chuyển đổi sang đơn vị xu
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid amount format: " + e.getMessage());
        }

        // Tạo thông điệp thanh toán
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("clientId", PayosConfig.clientId);
        paymentRequest.put("amount", amount);
        paymentRequest.put("currency", "VND");
        paymentRequest.put("orderId", PayosConfig.getRandomNumber(8));
        paymentRequest.put("returnUrl", PayosConfig.returnUrl);

        // Tạo checksum
        String checksum = generateChecksum(paymentRequest, PayosConfig.checksumKey);
        paymentRequest.put("checksum", checksum);

        // Gửi yêu cầu đến API thanh toán
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("apiKey", PayosConfig.apiKey); // Gửi apiKey trong header
        headers.set("clientId", PayosConfig.clientId);
        headers.set("checksumKey", PayosConfig.checksumKey);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(PayosConfig.payUrl, entity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            // Xử lý phản hồi từ Payos
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Payment request failed: " + response.getBody());
        }
    }

    private String generateChecksum(Map<String, Object> params, String key) {
        // Create a string from the parameters
        StringBuilder sb = new StringBuilder();
        params.entrySet().stream()
            .sorted(Map.Entry.comparingByKey()) // Ensure the order is consistent
            .forEach(entry -> sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&"));
        sb.append("key=").append(key); // Append the key at the end
        return sha256(sb.toString());
    }

    private String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}