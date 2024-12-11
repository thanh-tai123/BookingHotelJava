package com.poly.entity;

import java.util.Date;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
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
@Table(name = "discount")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	    
	    private int discountRate; // tỷ lệ giảm giá (15% sẽ là 0.15)
	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date startDate;

	    @DateTimeFormat(pattern = "yyyy-MM-dd")
	    private Date endDate;
	    
	    @ManyToOne
	    @JoinColumn(name = "roomid", nullable = false)
	    @JsonBackReference
	    private Room room;
}
