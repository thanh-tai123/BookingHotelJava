package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poly.entity.RoomTypeByService;
@Repository
public interface RoomTypeByServiceRepository extends JpaRepository<RoomTypeByService, Integer>{

}
