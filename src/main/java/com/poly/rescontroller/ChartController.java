package com.poly.rescontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.service.BookDetailService;

@RestController
@RequestMapping("/api")
public class ChartController {

    @Autowired
    private BookDetailService bookDetailService;

    @GetMapping("/revenue")
    public List<Map<String, Object>> getRevenueData(@RequestParam Integer year) {
        return bookDetailService.getMonthlyRevenueByYear(year);
    }

    @GetMapping("/roomcount")
    public List<Map<String, Object>> getRoomCountData(@RequestParam Integer year) {
        return bookDetailService.getMonthlyRoomCountByYear(year);
    }
}