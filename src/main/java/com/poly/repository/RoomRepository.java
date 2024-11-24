package com.poly.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.poly.dto.RoomStatisticsDTO;
import com.poly.entity.Room;
import com.poly.util._enum.RoomStatus;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer>, JpaSpecificationExecutor<Room> {
	List<Room> findByStatus(RoomStatus status);
	Optional<Room> findById(int id);
	@Query("SELECT r FROM Room r WHERE r.hotel.id = :hotelId " +
			"AND r.status = :status " +
			"AND r.id NOT IN (" +
			"    SELECT bd.room.id FROM BookDetail bd " +
			"    WHERE bd.checkin < :checkout AND bd.checkout > :checkin" +
			")")
	Page<Room> findAvailableRooms(
			@Param("hotelId") int hotelId,
			@Param("checkin") Date checkin,
			@Param("checkout") Date checkout,
			@Param("status") RoomStatus status,
			Pageable pageable
	);
	 @Query("SELECT r FROM Room r LEFT JOIN FETCH r.roomtype rt WHERE r.id NOT IN :ids AND r.status = :status")
	List<Room> findAvailableRoomsExcludingIds(@Param("ids") List<Integer> ids, @Param("status") RoomStatus status);

	Page<Room> findByStatus(RoomStatus status, Pageable pageable);
	List<Room> findByRoomtype_Name(String name);
	@Query("SELECT r.roomtype.name, COUNT(r) FROM Room r GROUP BY r.roomtype")
	List<Object[]> countRoomsByRoomType();
	@Query("SELECT r.hotel.chinhanh, COUNT(r) FROM Room r GROUP BY r.hotel.chinhanh")
	List<Object[]> countRoomsByBranch();


}