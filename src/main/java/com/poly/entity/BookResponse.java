package com.poly.entity;

public class BookResponse {
	  public BookResponse(Room room) {
		  this.roomId = room.getId();
	        this.roomType = room.getKieuphong();
	        this.roomPrice = room.getGia();
	        this.roomDescription = room.getMota();
	}
	private int roomId;
	    private String roomType;
	    private float roomPrice;
	    private String roomDescription;
}
