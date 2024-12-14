package com.poly.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.dto.ServiceDTO;
import com.poly.entity.Role;
import com.poly.entity.Services;
import com.poly.repository.ServiceRepository;
import com.poly.serviceRepository.ServiceServiceRepository;

@Service
public class ServiceService implements ServiceServiceRepository{
    @Autowired
    private ServiceRepository serviceRepository;

    public Services createService(ServiceDTO serviceDTO) {
    	 String upperCaseName = serviceDTO.getName().toUpperCase();
    	 if(serviceRepository.existsByName(upperCaseName)){
    		 throw new IllegalArgumentException("Dịch vụ " + upperCaseName + " đã tồn tại");
    	 }
        Services service = new Services();
        service.setName(upperCaseName);
        service.setDescription(serviceDTO.getDescription());
        return serviceRepository.save(service);
    }

    public List<ServiceDTO> getAllServices() {
        return serviceRepository.findAll().stream()
                .map(service -> {
                    ServiceDTO dto = new ServiceDTO();
                    dto.setId(service.getId());
                    dto.setName(service.getName());
                    dto.setDescription(service.getDescription());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ServiceDTO updateService(Integer id, ServiceDTO serviceDTO) {
        Services existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id " + id));
        
        existingService.setName(serviceDTO.getName());
        existingService.setDescription(serviceDTO.getDescription());
        
        Services updatedService = serviceRepository.save(existingService);
        serviceDTO.setId(updatedService.getId());
        return serviceDTO;
    }

    public void deleteService(Integer id) {
        serviceRepository.deleteById(id);
    }
    public List<Services> findAll() {
        return this.serviceRepository.findAll();
    }
}