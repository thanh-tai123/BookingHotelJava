package com.poly.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.entity.Discount;
import com.poly.repository.DiscountRepository;

@Service
public class DiscountService {

	@Autowired
    private DiscountRepository discountRepository;

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Discount getDiscountById(Integer id) {
        return discountRepository.findById(id).orElse(null);
    }

    public void saveDiscount(Discount discount) {
        discountRepository.save(discount);
    }

    public void deleteDiscount(Integer id) {
        discountRepository.deleteById(id);
    }
}