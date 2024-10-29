package com.poly.service;

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
}