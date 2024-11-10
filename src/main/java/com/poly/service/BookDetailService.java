package com.poly.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.BookDetail;
import com.poly.entity.Room;
import com.poly.repository.BookDetailRepository;

@Service
public class BookDetailService {

    @Autowired
    private BookDetailRepository bookDetailRepository;

    public List<Map<String, Object>> getMonthlyRevenueByYear(Integer year) {
        List<Object[]> results = bookDetailRepository.findMonthlyRevenueByYear(year);
        return results.stream()
                .map(result -> Map.of("month", result[1], "revenue", result[2]))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getMonthlyRoomCountByYear(Integer year) {
        List<Object[]> results = bookDetailRepository.findMonthlyRoomCountByYear(year);
        return results.stream()
                .map(result -> Map.of("month", result[1], "roomCount", result[2]))
                .collect(Collectors.toList());
    }

    
    public List<BookDetail> getBookingsByRoomid(Room room) {
        return bookDetailRepository.findByRoom(room);
    }
}