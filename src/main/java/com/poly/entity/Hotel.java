package com.poly.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
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
public class Hotel {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

        private String chinhanh;
		private String diachi;
	    @OneToMany(mappedBy = "hotel")
	    @JsonManagedReference
	    private List<Room> rooms;


}
