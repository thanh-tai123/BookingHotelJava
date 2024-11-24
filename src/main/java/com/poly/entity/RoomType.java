package com.poly.entity;

import java.util.HashSet;
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
import jakarta.persistence.Transient;
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
public class RoomType {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Integer id;
	  	private String name;
	  	private String description;
	  	 @OneToMany(mappedBy = "roomtype")
		    @JsonManagedReference
		    @ToString.Exclude
		    private List<Room> rooms;
	  	@OneToMany(mappedBy = "myroomType")
	  	 @JsonBackReference
	  	@ToString.Exclude
	    private List<RoomTypeByService> services;
	  	
	  	
	  	 @Transient
	     private Set<Services> roomservices = new HashSet<>();

	     public Set<Integer> getRoomserviceIds() {
	         Set<Integer> ids = new HashSet<>();
	         for (RoomTypeByService rts : this.services) {
	             ids.add(rts.getMyService().getId());
	         }
	         return ids;
	     }
//	  	@OneToMany(mappedBy = "roomType")
//	    private Set<RoomTypeByService> services;

}
