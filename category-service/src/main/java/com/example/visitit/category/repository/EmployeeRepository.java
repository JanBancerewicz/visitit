package com.example.visitit.category.repository;

import com.example.visitit.category.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>
{
    Optional<Employee> findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndRoleIgnoreCase(String f, String l, String r);

}
