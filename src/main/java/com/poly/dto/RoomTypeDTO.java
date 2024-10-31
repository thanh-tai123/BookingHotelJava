package com.poly.dto;

import java.util.Date;

import com.poly.util._enum.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeDTO {
	 private Integer id;
    private String name;
    private String description;

    // Getters and Setters
}