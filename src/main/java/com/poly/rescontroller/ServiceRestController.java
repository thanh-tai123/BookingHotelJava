package com.poly.rescontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Services createService(@RequestBody ServiceDTO serviceDTO) {
        return serviceService.createService(serviceDTO);
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