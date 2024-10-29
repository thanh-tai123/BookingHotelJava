package com.poly.dto;

import com.poly.util._enum.RoomStatus;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomTypeDTO {
    private String name;
    private String description;

    // Getters and Setters
}