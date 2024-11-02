package com.poly.entity;


import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name="service")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Services {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	  	private String name;
	  	private String description;
	  	 @OneToMany(mappedBy = "myService") 
	  	 @JsonManagedReference
	     private List<RoomTypeByService> services;

		/*
		 * @OneToMany(mappedBy = "service") private Set<RoomTypeByService> roomTypes;
		 */
}
