package com.poly.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.RoomTypeByService;
@Repository
public interface RoomTypeByServiceRepository extends JpaRepository<RoomTypeByService, Integer>{
	@Query("SELECT r.myroomType.name, r.myService.name " +
	           "FROM RoomTypeByService r")
	    List<Object[]> findRoomTypeServiceDetails();
	    
	    
	    @Query("SELECT r FROM RoomTypeByService r WHERE r.myroomType.id = :roomTypeId")
	    List<RoomTypeByService> findByRoomTypeId(@Param("roomTypeId") Integer roomTypeId);
}
