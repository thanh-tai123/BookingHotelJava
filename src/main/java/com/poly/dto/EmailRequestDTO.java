package com.poly.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.poly.entity.BookDetail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data

@AllArgsConstructor
@NoArgsConstructor
public class EmailRequestDTO {
    private String txtTo;
    private String txtCC;
    private String txtSubject;
    private String bookCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date createDate;
    private List<BookDetail> bookDetails;
    // Getters và Setters
}