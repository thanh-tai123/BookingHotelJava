package com.poly.serviceRepository;

import java.util.List;

import com.poly.dto.ServiceDTO;
import com.poly.entity.Services;

public interface ServiceServiceRepository {
	public Services createService(ServiceDTO serviceDTO) ;
	 public List<ServiceDTO> getAllServices();
	 public ServiceDTO updateService(Integer id, ServiceDTO serviceDTO);
	 public void deleteService(Integer id) ;
	 public List<Services> findAll() ;
}
