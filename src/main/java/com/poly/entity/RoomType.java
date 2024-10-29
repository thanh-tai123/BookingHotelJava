package com.poly.entity;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
public class RoomType {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	  	private String name;
	  	private String description;
	  	 @OneToMany(mappedBy = "roomtype")
		    @JsonManagedReference
		    private List<Room> rooms;
	  	@OneToMany(mappedBy = "myroomType")
	  	 @JsonBackReference
	    private List<RoomTypeByService> services;
//	  	@OneToMany(mappedBy = "roomType")
//	    private Set<RoomTypeByService> services;

}
