package com.poly.serviceRepository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.poly.dto.RoomDTO;
import com.poly.dto.RoomRequest;
import com.poly.dto.RoomStatisticsDTO;
import com.poly.entity.Room;
import com.poly.util._enum.RoomStatus;

public interface RoomServiceRepository {
	 public Room findById(int id) ;
	  public void updateRoom(int roomId, RoomRequest roomRequest, List<MultipartFile> images);
	  public boolean deleteRoom(int id);
	  public void addRoom(RoomRequest roomRequest, List<MultipartFile> images);
	  public List<RoomDTO> getRoomsByStatus(RoomStatus status);
	  public RoomDTO getRoomDetails(int roomId);
	  public List<RoomDTO> getAvailableRooms(Date checkin, Date checkout, RoomStatus status);
	  public List<Room> findByRoomType(String roomtype);
	  public List<Room> findAll();
	  public void viewRoomDetails(int roomId, Integer userId);
	  public int getVisitCount(int roomId);
	  public Map<String, Long> getRoomTypeCounts();
	  public List<RoomStatisticsDTO> getRoomStatistics();
}
