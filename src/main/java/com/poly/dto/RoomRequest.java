package com.poly.dto;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.util._enum.RoomStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
//RoomRequest.java
public class RoomRequest {

private int hotelid;
 private int sophong;
 private String kieuphong;
 private float gia;

 private String mota;
 private RoomStatus status = RoomStatus.FALSE;
// private String note;
private Long staffid;
private Integer roomtypeid;

 // Getters and setters
}
