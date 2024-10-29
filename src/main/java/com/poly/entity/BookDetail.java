package com.poly.entity;

import java.time.LocalDate;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class BookDetail {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	private Float total;
    private Integer bookid;
//    private Integer booklist;
    private Integer roomid;

    private Float price;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date checkin;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date checkout;
    private Integer adult;

    private Integer children;
    private String paymentMethod;
    private String paymentStatus;
    @ManyToOne
    @JoinColumn(name = "bookid", insertable = false, updatable = false)
    private Book book;
    
//    @ManyToOne
//    @JoinColumn(name = "booklist", insertable = false, updatable = false)
//    private Book booklistroom;
 //   @ManyToOne
//    @JoinColumn(name = "bookid", insertable = false, updatable = false)
//    private Book mybook;
}
