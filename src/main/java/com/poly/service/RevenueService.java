package com.poly.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.RevenueComparisonDTO;
import com.poly.repository.BookRepository;

@Service
public class RevenueService {
    @Autowired
    private BookRepository bookRepository;

    public List<RevenueComparisonDTO> getRevenueComparison(int year1, int year2) {
        List<RevenueComparisonDTO> result = new ArrayList<>();

        // Query for revenue in year1
        Float revenueYear1 = bookRepository.calculateRevenueByYear(year1);
        result.add(new RevenueComparisonDTO(year1 + "", revenueYear1 != null ? revenueYear1 : 0f));

        // Query for revenue in year2
        Float revenueYear2 = bookRepository.calculateRevenueByYear(year2);
        result.add(new RevenueComparisonDTO(year2 + "", revenueYear2 != null ? revenueYear2 : 0f));

        return result;
    }
}