package com.poly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import vn.payos.PayOS;

@Configuration
public class PayOSConfig {

    @Value("${PAYOS_CLIENT_ID}")
    public static String clientId;

    @Value("${PAYOS_API_KEY}")
    public String apiKey;

    @Value("${PAYOS_CHECKSUM_KEY}")
    public String checksumKey;

    @Bean
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}