package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.Service;



public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
