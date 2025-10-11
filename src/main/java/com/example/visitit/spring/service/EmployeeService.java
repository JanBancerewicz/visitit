package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Employee;
import com.example.visitit.spring.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(UUID.randomUUID());
        }
        return employeeRepository.save(employee);
    }

    public void delete(Employee employee) {
        employeeRepository.delete(employee);
    }

    public void deleteById(UUID id) {
        employeeRepository.deleteById(id);
    }
}
