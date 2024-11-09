package com.poly.dto;

import com.poly.util._enum.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
	private int id;
	private String roomCode;
    private HotelDTO hotelid;
    private int roomtypeid;
    private String img;
    private int sophong;
    private String kieuphong;
    private RoomTypeDTO roomType;
    private float gia;
    private String mota;
    private RoomStatus status;
    private String note;
    private UserDTO staffid;
}
