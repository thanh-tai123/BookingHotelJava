package com.poly.dto;

import java.util.Date;

import com.poly.entity.Hotel;
import com.poly.entity.Room;
import com.poly.entity.RoomType;
import com.poly.util._enum.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data

@AllArgsConstructor
@NoArgsConstructor
public class BookDetailResponse {
    private int roomId;
    private float price;
    private Date checkin;
    private Date checkout;
    private float total;
    private int adult;
    private int children;
    private String paymentMethod;
    private String paymentStatus;

    // Getters and Setters
}