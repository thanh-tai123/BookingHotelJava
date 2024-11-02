package com.poly.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.RoomTypeDTO;

import com.poly.dto.RoomTypeServiceSummaryDTO;
import com.poly.entity.RoomType;
import com.poly.entity.RoomTypeByService;
import com.poly.entity.Services;
import com.poly.repository.RoomTypeByServiceRepository;
import com.poly.repository.RoomTypeRepository;

@Service
public class RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomTypeByServiceRepository roomTypeByServiceRepository;
    
    public RoomType createRoomType(RoomTypeDTO roomTypeDTO) {
        RoomType roomType = new RoomType();
        roomType.setName(roomTypeDTO.getName());
        roomType.setDescription(roomTypeDTO.getDescription());
        return roomTypeRepository.save(roomType);
    }
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeRepository.findAll().stream()
                .map(roomType -> {
                    RoomTypeDTO dto = new RoomTypeDTO();
                    dto.setId(roomType.getId());
                    dto.setName(roomType.getName());
                    dto.setDescription(roomType.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public RoomTypeDTO updateRoomType(Integer id, RoomTypeDTO roomTypeDTO) {
        RoomType existingRoomType = roomTypeRepository.findById(id)
                .orElseThrow();
        
        existingRoomType.setName(roomTypeDTO.getName());
        existingRoomType.setDescription(roomTypeDTO.getDescription());
        
        RoomType updatedRoomType = roomTypeRepository.save(existingRoomType);
        roomTypeDTO.setId(updatedRoomType.getId());
        return roomTypeDTO;
    }	

    public void deleteRoomType(Integer id) {
        roomTypeRepository.deleteById(id);
    }

    public List<RoomTypeServiceSummaryDTO> getRoomTypeServiceSummaries() {
        List<Object[]> results = roomTypeByServiceRepository.findRoomTypeServiceDetails();
        Map<String, List<String>> roomTypeServiceMap = new HashMap<>();

        // Gộp các dịch vụ theo loại phòng
        for (Object[] result : results) {
            String roomTypeName = (String) result[0];
            String serviceName = (String) result[1];

            roomTypeServiceMap.computeIfAbsent(roomTypeName, k -> new ArrayList<>()).add(serviceName);
        }

        // Tạo danh sách DTO
        List<RoomTypeServiceSummaryDTO> summaries = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : roomTypeServiceMap.entrySet()) {
            String roomTypeName = entry.getKey();
            List<String> services = entry.getValue();
            long serviceCount = services.size();
            summaries.add(new RoomTypeServiceSummaryDTO(roomTypeName, services, serviceCount));
        }

        return summaries;
    }
   

    public List<Services> getServicesByRoomType(Integer roomTypeId) {
        List<RoomTypeByService> roomTypeByServices = roomTypeByServiceRepository.findByRoomTypeId(roomTypeId);
        return roomTypeByServices.stream()
                .map(RoomTypeByService::getMyService)
                .collect(Collectors.toList());
    }

    
}