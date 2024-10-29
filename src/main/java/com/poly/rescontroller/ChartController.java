package com.poly.rescontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.service.BookDetailService;

@RestController
public class ChartController {

    @Autowired
    private BookDetailService bookDetailService;

    @GetMapping("/api/revenue")
    public List<Map<String, Object>> getRevenueData() {
        return bookDetailService.getMonthlyRevenue();
    }
    @GetMapping("/api/roomCount")
    public List<Map<String, Object>> getRoomCountData() {
        return bookDetailService.getMonthlyRoomCount();
    }
}