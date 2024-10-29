package com.poly.entity;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.poly.util._enum.RoomStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
		  @ManyToOne
		    @JoinColumn(name = "hotelid", nullable = false)
		    @JsonBackReference
		    private Hotel hotel;
		   @ManyToOne
		    @JoinColumn(name = "roomtypeid", nullable = false)
		    @JsonBackReference
		    private RoomType roomtype;
	    private String img;
//	    private String diachi;
	    private int sophong;
	    private String kieuphong;
	    private float gia;
	    private String mota;
	    @Builder.Default
	    @Enumerated(EnumType.STRING)
	    private RoomStatus status = RoomStatus.FALSE;
	    private String note;
	    private int staffid;
	 
}
