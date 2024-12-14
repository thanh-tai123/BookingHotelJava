package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.poly.entity.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
	 
	boolean existsByName(String name);
}