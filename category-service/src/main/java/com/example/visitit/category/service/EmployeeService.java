package com.example.visitit.category.service;

import com.example.visitit.category.model.Employee;
import com.example.visitit.category.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repo;

    public List<Employee> findAll() {
        return repo.findAll();
    }

    public Optional<Employee> findById(UUID id) {
        return repo.findById(id);
    }

    @Transactional
    public Employee create(Employee in) {
        // wymuś INSERT – ID ma wygenerować JPA (@GeneratedValue)
        in.setId(null);
        return repo.save(in);
    }

    @Transactional
    public Optional<Employee> update(UUID id, Employee in) {
        return repo.findById(id).map(existing -> {
            existing.setFirstName(in.getFirstName());
            existing.setLastName(in.getLastName());
            existing.setRole(in.getRole());
            existing.setDescription(in.getDescription());
            return repo.save(existing);
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        return repo.findById(id)
                .map(entity -> {
                    repo.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}
