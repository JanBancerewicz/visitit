package com.example.visitit.category.service;

import com.example.visitit.category.model.ServiceEntity;
import com.example.visitit.category.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class ServiceService {
    private final ServiceRepository repo;

    public List<ServiceEntity> findAll(){ return repo.findAll(); }
    public Optional<ServiceEntity> findById(UUID id){ return repo.findById(id); }

    @Transactional
    public ServiceEntity create(ServiceEntity in){
        if(in.getId()==null) in.setId(UUID.randomUUID());
        if(repo.existsByName(in.getName())) throw new IllegalStateException("NAME_CONFLICT");
        return repo.save(in);
    }

    @Transactional
    public Optional<ServiceEntity> update(UUID id, ServiceEntity in){
        return repo.findById(id).map(ex -> {
            if(repo.existsByNameAndIdNot(in.getName(), id)) throw new IllegalStateException("NAME_CONFLICT");
            ex.setName(in.getName());
            ex.setDescription(in.getDescription());
            ex.setDurationMin(in.getDurationMin());
            ex.setPrice(in.getPrice());
            return repo.save(ex);
        });
    }

    @Transactional
    public boolean delete(UUID id){
        return repo.findById(id).map(e->{ repo.delete(e); return true; }).orElse(false);
    }
}
