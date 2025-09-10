package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    // możesz tu dopisać metody wyszukiwania po roli, nazwisku itd.
}
