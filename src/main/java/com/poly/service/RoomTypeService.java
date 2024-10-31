package com.poly.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.RoomTypeDTO;
import com.poly.entity.RoomType;
import com.poly.repository.RoomTypeRepository;

@Service
public class RoomTypeService {
    @Autowired
    private RoomTypeRepository roomTypeRepository;

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
}