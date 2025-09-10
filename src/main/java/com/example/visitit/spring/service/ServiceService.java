package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Service;
import com.example.visitit.spring.repository.ServiceRepository;
//import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Service
public class ServiceService {

    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    public Service save(Service service) {
        if (service.getId() == null) {
            service.setId(UUID.randomUUID());
        }
        return serviceRepository.save(service);
    }

    public void delete(Service service) {
        serviceRepository.delete(service);
    }
}
