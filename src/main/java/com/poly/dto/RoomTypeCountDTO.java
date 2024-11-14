package com.poly.dto;

import java.util.Date;
import java.util.List;

import com.poly.entity.BookDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data

@AllArgsConstructor
@NoArgsConstructor
public class RoomTypeCountDTO {
	 private String roomTypeName;
	    private long count;
}
