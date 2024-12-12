package com.poly.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.entity.User;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BookingRequest {
	 private String email;
	private Long Userid;
	  private List<Integer> roomid;
	  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
	  private Date checkin;

	  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
	  private Date checkout;
	  private String bookCode;
    private int adult;
    private int children;
    private float price;
    private float total;
    private String paymentMethod;
    private String paymentStatus;
    private String bookDetailStatus="notcheckin";
    private List<RoomRequest> rooms = new ArrayList<>(); 
    // Getters and Setters
}
