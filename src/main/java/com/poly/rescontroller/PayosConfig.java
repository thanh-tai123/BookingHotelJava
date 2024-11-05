package com.poly.rescontroller;



import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;




import lombok.Data;

public class PayosConfig {
    public static String payUrl = "https://api-merchant.payos.vn/v2/payment-requests";
    public static String returnUrl = "http://localhost:8081/room/displaypayment";
    public static String clientId = "a44605ee-acd2-41ba-b326-745e08190618";
    public static String checksumKey = "aa7d970a94dfa7ca2f30c0a555b5c3290227636ff1d889d94b8e63baa3dc121a";
    public static String apiKey = "ec10c791-5279-491e-a4a7-5f5f121a650b";
    
    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
    public static String Sha256(String message) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(message.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                sb.append(String.format("%02x", b & 0xff));
            }
            digest = sb.toString();
        } catch (UnsupportedEncodingException ex) {
            digest = "";
        } catch (NoSuchAlgorithmException ex) {
            digest = "";
        }
        return digest;
    }
}