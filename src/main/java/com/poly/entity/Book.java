//package com.poly.entity;
//
//
//
//import java.security.SecureRandom;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//
//import jakarta.persistence.CascadeType;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import jakarta.persistence.OneToMany;
//import jakarta.persistence.Table;
//import jakarta.persistence.Temporal;
//import jakarta.persistence.TemporalType;
//import lombok.*;
//
//@Entity
//@Table
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class Book {
//	  @Id
//	    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	    private Integer id;
//	  @Column(name = "book_code")
//	  private String bookCode;
//		  @ManyToOne
//		    @JoinColumn(name = "Userid", nullable = false)
//		    @JsonBackReference
//		    private User user;
//	    @Column(name = "create_date")
//	    @Temporal(TemporalType.TIMESTAMP)
//	    private Date createDate;
//
//	    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	    private Set<BookDetail> bookDetails ; // Primary collection
//
////	    @OneToMany(mappedBy = "booklist", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////	    private List<BookDetail> booklistroom ;
//	    // Getter to convert Set to List
//
//	    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
//	    private static final SecureRandom RANDOM = new SecureRandom();
//
//	    public String generateBookCode() {
//	        StringBuilder code = new StringBuilder(6);
//	        for (int i = 0; i < 8; i++) {
//	            code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
//	        }
//	        this.bookCode = code.toString();
//	        return this.bookCode;
//	    }
////		  @OneToMany(mappedBy = "mybook")
////		  private List<BookDetail> mybookDetails;
////
////	    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
////	    private Set<BookDetail> bookDetails = new HashSet<>();
////	    @OneToMany(mappedBy = "book")
////	    private List<BookDetail> bookDetails;
////	    @Column(name = "Userid")
////	    private Long userId;
//
//}

package com.poly.entity;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

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
public class Book {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "book_code")
	private String bookCode;

	@ManyToOne
	@JoinColumn(name = "Userid", nullable = false)
	@JsonBackReference
	@ToString.Exclude // Loại trừ thuộc tính user khỏi toString()
	private User user;

	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	@ToString.Exclude // Loại trừ bookDetails để tránh vòng lặp
	private Set<BookDetail> bookDetails = new HashSet<>();

	private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();

	// Generate a random book code
	public String generateBookCode() {
		StringBuilder code = new StringBuilder(8);
		for (int i = 0; i < 8; i++) {
			code.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
		}
		this.bookCode = code.toString();
		return this.bookCode;
	}
}

