package com.poly.rescontroller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.RevenueComparisonDTO;
import com.poly.service.BookDetailService;
import com.poly.service.RevenueService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/api")
public class ChartController {
	 @Autowired
	    private RevenueService revenueService;
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
    @GetMapping("/top-users/{year}/{month}")
    public  List<Map<String, Object>> getTopUsers(@PathVariable int year, @PathVariable int month) {
        return bookDetailService.getTopUsersByMonthAndYear(year, month);
    }
    
    @GetMapping("/compare/{year1}/{year2}")
    public ResponseEntity<List<RevenueComparisonDTO>> compareRevenue(@PathVariable int year1, @PathVariable int year2) {
        List<RevenueComparisonDTO> comparison = revenueService.getRevenueComparison(year1, year2);
        return ResponseEntity.ok(comparison);
    }
    
    @GetMapping("/branch/{year}")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByBranchAndYear(@PathVariable int year) {
        List<Map<String, Object>> revenue = bookDetailService.getRevenueByBranchAndYear(year);
        return ResponseEntity.ok(revenue);
    }
}