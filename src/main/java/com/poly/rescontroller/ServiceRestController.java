package com.poly.rescontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.entity.Service;
import com.poly.repository.ServiceRepository;

@RestController
@RequestMapping("/api")
public class ServiceRestController {

    @Autowired
    private ServiceRepository serviceRepository;

    @GetMapping("/services")
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }
}