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
public class TableRoomPerBranchDTO {
	 private String roomCode;
	    private Float price;
	    private String img;
	    private String roomType;
	    private String branch;
	    private Date checkin;
	    private Date checkout;
}
