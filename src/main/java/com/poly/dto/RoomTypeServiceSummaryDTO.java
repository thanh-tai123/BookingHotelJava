package com.poly.dto;

import java.util.List;

import com.poly.util._enum.RoomStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeServiceSummaryDTO {
	private String roomTypeName;
    private List<String> services;
    private long serviceCount;

}
