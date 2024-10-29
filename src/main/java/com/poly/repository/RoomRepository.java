package com.poly.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.entity.Room;
import com.poly.util._enum.RoomStatus;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>{
	 List<Room> findByStatus(RoomStatus status);
	 
	 @Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId " +
		       "AND r.status = :status " +
		       "AND r.id NOT IN (" +
		       "    SELECT bd.roomid FROM BookDetail bd " +
		       "    WHERE bd.checkin < :checkout AND bd.checkout > :checkin" +
		       ")")
		List<Room> findAvailableRooms(
		    @Param("hotelId") int hotelId, 
		    @Param("checkin") Date checkin, 
		    @Param("checkout") Date checkout,
		    @Param("status") RoomStatus status // Truyền thêm tham số status
		);
	 @Query("SELECT r FROM Room r WHERE r.id NOT IN :ids AND r.status = :status")
	 List<Room> findAvailableRoomsExcludingIds(@Param("ids") List<Integer> ids, @Param("status") RoomStatus status);

}
