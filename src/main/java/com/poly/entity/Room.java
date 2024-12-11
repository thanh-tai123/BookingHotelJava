package com.poly.entity;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.poly.util._enum.RoomStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Room {
		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
		 private String roomCode;
		 
		  @ManyToOne
		    @JoinColumn(name = "hotelid", nullable = false)
		    @JsonBackReference
		    private Hotel hotel;
		   @ManyToOne
		    @JoinColumn(name = "roomtypeid", nullable = false)
		    @JsonBackReference
		    private RoomType roomtype;
	    private String img;
	    private String address;
	    private int sophong;
//	    private String kieuphong;
	    private float gia;
	    private String mota;
	    @Builder.Default
	    @Enumerated(EnumType.STRING)
	    private RoomStatus status = RoomStatus.FALSE;
	    private String note;
//	    private int staffid;
	    @ManyToOne
		  @JsonBackReference
		@ToString.Exclude
	    @JoinColumn(name = "staffid", referencedColumnName = "id", nullable = false)
	    private User user;
	    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    @JsonManagedReference
	    private List<BookDetail> bookDetails;
	    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    @JsonManagedReference
	    private List<Comment> comments;
	    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
	    @JsonManagedReference
	    private List<RoomImages> roomImages = new ArrayList<>();
	    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	    @JsonManagedReference
	    private List<Discount> discounts = new ArrayList<>();
	    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		private static final SecureRandom RANDOM = new SecureRandom();

		// Generate a random book code
		public String generateRoomCode() {
			StringBuilder code = new StringBuilder(8);
			for (int i = 0; i < 8; i++) {
				code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
			}
			this.roomCode = code.toString();
			return this.roomCode;
		}
		 public float getCurrentPrice(Date date) {
		        for (Discount discount : discounts) {
		            if (date.compareTo(discount.getStartDate()) >= 0 && date.compareTo(discount.getEndDate()) <= 0) {
		                return (gia * (100 - discount.getDiscountRate()))/100;
		            }
		        }
		        return gia;
		    }
}
