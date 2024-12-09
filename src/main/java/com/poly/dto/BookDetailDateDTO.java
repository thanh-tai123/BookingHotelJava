package com.poly.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data

@AllArgsConstructor
@NoArgsConstructor
public class BookDetailDateDTO {
		
    private Date checkin;
	  
    private Date checkout;
    private String bookDetailStatus;
}