package com.poly.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInfo {
	 private int amount;
	    private String description;
	    private Long orderCode;
	    private String status;
	    private String checkoutUrl;
	    private String qrCode;
	    private Book bookingInfo;
}
