package com.poly.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
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
public class Comment {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;

	    @ManyToOne
	    @JoinColumn(name = "roomid", nullable = false)
	    @JsonBackReference
	    private Room room;

	    private String username;
	    @Column(columnDefinition = "nvarchar(MAX)") // Specify nvarchar
	  
	    private String content;
	    private int rating; // số sao
	    private Date createdAt;
	    @Column(columnDefinition = "nvarchar(MAX)") // Admin response
	    private String adminResponse;
}
