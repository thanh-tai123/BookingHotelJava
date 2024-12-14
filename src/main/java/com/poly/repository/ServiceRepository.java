package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Services;



public interface ServiceRepository extends JpaRepository<Services, Integer> {
	  boolean existsByName(String name);
}
