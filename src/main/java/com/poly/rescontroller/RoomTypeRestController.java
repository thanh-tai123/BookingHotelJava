package com.poly.rescontroller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.RoomTypeCountDTO;
import com.poly.dto.RoomTypeDTO;

import com.poly.dto.RoomTypeServiceSummaryDTO;
import com.poly.entity.RoomType;
import com.poly.entity.RoomTypeByService;
import com.poly.entity.Services;
import com.poly.repository.RoomTypeByServiceRepository;
import com.poly.repository.RoomTypeRepository;
import com.poly.repository.ServiceRepository;
import com.poly.service.RoomTypeService;



@RestController
@RequestMapping("/api")
public class RoomTypeRestController {
	 @Autowired
	    private RoomTypeService roomTypeService;
    @Autowired
    private RoomTypeRepository roomtyepeRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private RoomTypeByServiceRepository roomTypeByServiceRepository;
    @GetMapping("/roomtype")
    public List<RoomType> getAllRoomType() {
        return roomtyepeRepository.findAll();
    }
    @PostMapping("/add/roomtype")
    public ResponseEntity<RoomType> addRoomType(@RequestBody RoomTypeDTO roomTypeDTO) {
        RoomType newRoomType = roomTypeService.createRoomType(roomTypeDTO);
        return ResponseEntity.ok(newRoomType);
    }
    
 // Thay đổi kiểu dữ liệu của serviceIds thành List<Integer> để nhận các ID
   
    @GetMapping("/roomtypes")
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypes();
    }
    @PutMapping("/update/roomtype/{id}")
    public RoomTypeDTO updateRoomType(@PathVariable Integer id, @RequestBody RoomTypeDTO roomTypeDTO) {
        return roomTypeService.updateRoomType(id, roomTypeDTO);
    }

    @DeleteMapping("/delete/roomtype/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable Integer id) {
        roomTypeService.deleteRoomType(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/counts")
    public List<RoomTypeCountDTO> getRoomTypeCounts() {
        return roomTypeService.getRoomTypeCounts();
    }
}