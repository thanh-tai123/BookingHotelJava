package com.poly.dto;

import java.util.List;

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
public class BookingResponse {
    private String userEmail;
    private String userName;
    private List<BookDetailResponse> roomDetails;

    // Getters and Setters
}