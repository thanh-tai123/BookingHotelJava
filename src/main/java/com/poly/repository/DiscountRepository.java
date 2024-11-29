package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Discount;


public interface DiscountRepository extends JpaRepository<Discount, Integer> {
}