package com.example.visitit.category.service;

import com.example.visitit.category.model.Employee;
import com.example.visitit.category.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository repo;

    public List<Employee> findAll(){ return repo.findAll(); }
    public Optional<Employee> findById(UUID id){ return repo.findById(id); }

    @Transactional
    public Employee create(Employee in){
        if(in.getId()==null) in.setId(UUID.randomUUID());
        return repo.save(in);
    }

    @Transactional
    public Optional<Employee> update(UUID id, Employee in){
        return repo.findById(id).map(ex -> {
            ex.setFirstName(in.getFirstName());
            ex.setLastName(in.getLastName());
            ex.setRole(in.getRole());
            ex.setDescription(in.getDescription());
            return repo.save(ex);
        });
    }

    @Transactional
    public boolean delete(UUID id){
        return repo.findById(id).map(e->{ repo.delete(e); return true; }).orElse(false);
    }
}
