package com.poly.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import com.poly.repository.ServiceRepository;

import jakarta.transaction.Transactional;

@Service
public class RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private RoomTypeByServiceRepository roomTypeByServiceRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    
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
    public RoomType findById(Integer id) {
        return roomTypeRepository.findById(id).orElse(null);
    }

    
    public void updateRoomTypeServices(RoomType roomType, List<Integer> serviceIds) {
        // Lấy RoomType từ database nếu đã tồn tại
        RoomType existingRoomType = roomTypeRepository.findById(roomType.getId()).orElseThrow(() -> new RuntimeException("RoomType not found"));
        System.out.println(existingRoomType.toString());
        // Lấy danh sách RoomTypeByService hiện tại
        List<RoomTypeByService> existingServices = existingRoomType.getServices();

        // Tạo danh sách các dịch vụ mới cần thêm vào
        Set<Services> newServices = serviceIds != null ? serviceIds.stream()
                                              .map(id -> serviceRepository.findById(id).orElseThrow(() -> new RuntimeException("Service not found")))
                                              .collect(Collectors.toSet()) : Collections.emptySet();

        // Kiểm tra và thêm các dịch vụ mới nếu chưa tồn tại
        for (Services newService : newServices) {
            boolean exists = existingServices.stream()
                                             .anyMatch(existingService -> existingService.getMyService().getId().equals(newService.getId()));
            if (!exists) {
                RoomTypeByService roomTypeByService = new RoomTypeByService();
                roomTypeByService.setMyroomType(existingRoomType);
                roomTypeByService.setMyService(newService);
                roomTypeByServiceRepository.save(roomTypeByService);
            }
        }

        // Kiểm tra và xóa các dịch vụ không còn được chọn
        for (RoomTypeByService existingService : existingServices) {
            boolean stillSelected = newServices.stream()
                                               .anyMatch(newService -> newService.getId().equals(existingService.getMyService().getId()));
            if (!stillSelected) {
                // Chỉ xóa nếu thực thể tồn tại
                if (roomTypeByServiceRepository.existsById(existingService.getId())) {
                    roomTypeByServiceRepository.delete(existingService);
                }
            }
        }

        // Cập nhật RoomType
        existingRoomType.setName(roomType.getName());
        existingRoomType.setDescription(roomType.getDescription());

        roomTypeRepository.save(existingRoomType);
    }
    
}