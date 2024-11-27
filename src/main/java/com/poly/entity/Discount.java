package com.poly.entity;

import java.util.Date;
import java.util.Set;



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
	    private int id;
	    
	    private float discountRate; // tỷ lệ giảm giá (15% sẽ là 0.15)
	    
	    private Date startDate;
	    private Date endDate;
	    
	    @ManyToOne
	    @JoinColumn(name = "roomid", nullable = false)
	    @JsonBackReference
	    private Room room;
}
