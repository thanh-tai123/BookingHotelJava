package com.poly.serviceRepository;

import java.util.List;

import com.poly.dto.RoomTypeCountDTO;
import com.poly.dto.RoomTypeDTO;
import com.poly.dto.RoomTypeServiceSummaryDTO;
import com.poly.entity.RoomType;
import com.poly.entity.Services;

public interface RoomTypeServiceRepository {
	public RoomType createRoomType(RoomTypeDTO roomTypeDTO) ;
	public List<RoomTypeDTO> getAllRoomTypes();
	public RoomTypeDTO updateRoomType(Integer id, RoomTypeDTO roomTypeDTO);
	public void deleteRoomType(Integer id);
	 public List<RoomTypeServiceSummaryDTO> getRoomTypeServiceSummaries();
	 public List<Services> getServicesByRoomType(Integer roomTypeId);
	 public RoomType findById(Integer id);
	 public void updateRoomTypeServices(RoomType roomType, List<Integer> serviceIds);
	 public List<RoomType> getAllRoomType();
	 public List<RoomTypeCountDTO> getRoomTypeCounts();
}
