package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.RoomType;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {
}