package com.poly.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.BookDetail;
import com.poly.repository.BookDetailRepository;

@Service
public class BookDetailService {

    @Autowired
    private BookDetailRepository bookDetailRepository;

    public List<Map<String, Object>> getMonthlyRevenue() {
        List<Object[]> results = bookDetailRepository.findMonthlyRevenue();
        return results.stream()
                .map(result -> Map.of("month", result[0], "revenue", result[1]))
                .collect(Collectors.toList());
    }
    public List<Map<String, Object>> getMonthlyRoomCount() {
        List<Object[]> results = bookDetailRepository.findMonthlyRoomCount();
        return results.stream()
                .map(result -> Map.of("month", result[0], "roomCount", result[1]))
                .collect(Collectors.toList());
    }
    
    public List<BookDetail> getBookingsByRoomid(Long roomid) {
        return bookDetailRepository.findByRoomid(roomid);
    }
}