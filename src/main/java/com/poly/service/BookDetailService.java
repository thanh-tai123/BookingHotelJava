package com.poly.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.BookDetailDateDTO;
import com.poly.entity.BookDetail;
import com.poly.entity.Room;
import com.poly.repository.BookDetailRepository;
import com.poly.serviceRepository.BookDetailServiceRepository;

import jakarta.transaction.Transactional;

@Service
public class BookDetailService implements BookDetailServiceRepository{

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
    public List<Map<String, Object>> getTopUsersByMonthAndYear(int year, int month) {
        // Lấy kết quả từ repository
        List<Object[]> results = bookDetailRepository.findTopUsersByMonthAndYear(year, month);

        // Chuyển đổi kết quả sang một danh sách các Map
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userEmail", result[0]);    // Email người dùng
                    map.put("bookingCount", result[1]); // Số lượng đặt phòng
                    map.put("year", year);              // Năm
                    map.put("month", month);            // Tháng
                    return map;
                })
                .collect(Collectors.toList());
    }

    
    public List<BookDetailDateDTO> getBookingsByRoomid(Room room) {
        List<BookDetail> bookings = bookDetailRepository.findByRoom(room);
        return bookings.stream()
                       .filter(b -> !"cancel".equals(b.getBookDetailStatus()) && b.getCheckout().after(new Date()))
                       .map(b -> new BookDetailDateDTO(b.getCheckin(), b.getCheckout(), b.getBookDetailStatus()))
                       .collect(Collectors.toList());
    }


    
    public List<Map<String, Object>> getRevenueByBranchAndYear(Integer year) {
        return bookDetailRepository.findRevenueByBranchAndYear(year);
    }
    
    public List<Map<String, Object>> getRoomsBookedByBranch(String branchName) {
        return bookDetailRepository.findRoomsBookedByBranch(branchName);
    }
}