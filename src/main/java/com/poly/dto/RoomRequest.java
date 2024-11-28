package com.poly.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.poly.util._enum.RoomStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
//RoomRequest.java
public class RoomRequest {

    @NotNull(message = "Hotel ID is required")
    private Integer hotelid;

    @NotNull(message = "Room number is required")
    private Integer sophong;

    @NotNull(message = "Img is required")
    private String img;
    @NotNull(message = "Room type is required")
    private Integer roomtypeid;



   

    @NotNull(message = "Price is required")
    private Float gia;

    @Size(max = 255, message = "Description cannot be longer than 255 characters")
    private String mota;

    @NotNull(message = "Status is required")
    private RoomStatus status = RoomStatus.FALSE;

    @NotNull(message = "Staff ID is required")
    private Long staffid;
    private List<MultipartFile> images;
    // Getters and setters
}

