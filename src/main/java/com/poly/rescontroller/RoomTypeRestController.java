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

}