package com.poly.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.entity.BookDetail;
import com.poly.entity.RoomImages;

public interface RoomImagesRepository extends JpaRepository<RoomImages, Long>{

}
