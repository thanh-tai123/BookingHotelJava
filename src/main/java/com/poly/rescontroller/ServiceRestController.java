package com.poly.rescontroller;

import java.util.List;

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

import com.poly.dto.ServiceDTO;
import com.poly.entity.Services;
import com.poly.repository.ServiceRepository;
import com.poly.service.ServiceService;

@RestController
@RequestMapping("/api")
public class ServiceRestController {

    @Autowired
    private ServiceService serviceService;

    // Lấy tất cả các dịch vụ
    @GetMapping("/services")
    public List<ServiceDTO> getAllServices() {
        return serviceService.getAllServices();
    }

    // Lấy dịch vụ theo ID
   

    // Tạo dịch vụ mới
    @PostMapping("/add/services")
    public ResponseEntity<?> createService(@RequestBody ServiceDTO serviceDTO) {
        try {
            Services newService = serviceService.createService(serviceDTO);
            return ResponseEntity.ok(newService);
        } catch (IllegalArgumentException e) {
            // Trả về phản hồi lỗi 400 nếu tên dịch vụ đã tồn tại
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Trả về phản hồi lỗi 500 cho các lỗi không xác định
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    // Cập nhật dịch vụ
    @PutMapping("/update/services/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable Integer id, @RequestBody ServiceDTO serviceDTO) {
        ServiceDTO updatedService = serviceService.updateService(id, serviceDTO);
        return ResponseEntity.ok(updatedService);
    }

    // Xóa dịch vụ
    @DeleteMapping("/delete/services/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}