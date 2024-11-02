package com.poly.entity;

import java.util.List;

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
@Table
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomTypeByService {
	@Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	  
	  @ManyToOne
	    @JoinColumn(name = "roomTypeid")
	    private RoomType myroomType;
	  @ManyToOne
	    @JoinColumn(name = "serviceid")
	  @JsonBackReference
	    private Services myService; 
	    //private Integer serviceid;

		
}
