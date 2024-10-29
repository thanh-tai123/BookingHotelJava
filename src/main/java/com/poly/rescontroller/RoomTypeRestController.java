package com.poly.rescontroller;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.dto.RoomTypeDTO;
import com.poly.entity.RoomType;
import com.poly.entity.RoomTypeByService;
import com.poly.entity.Service;
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
    @PostMapping("/{roomTypeId}/services")
    public ResponseEntity<?> assignServicesToRoomType(@PathVariable RoomType roomTypeId, @RequestBody List<Integer> serviceIds) {
        // Kiểm tra các serviceIds trước khi lưu
        if (serviceIds == null || serviceIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "No services provided"));
        }

        // Tìm kiếm các dịch vụ từ cơ sở dữ liệu
        List<Service> services = serviceRepository.findAllById(serviceIds);
        
        // Thêm các dịch vụ mới
        for (Service service : services) {
            RoomTypeByService roomTypeByService = new RoomTypeByService();
            roomTypeByService.setMyroomType(roomTypeId);
            roomTypeByService.setMyService(service); // Gán service đúng
            roomTypeByServiceRepository.save(roomTypeByService);
        }

        return ResponseEntity.ok(Collections.singletonMap("message", "Services assigned successfully"));
    }
}