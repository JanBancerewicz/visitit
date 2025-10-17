package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.ref.EmployeeRef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EmployeeRefRepository extends JpaRepository<EmployeeRef, UUID> {}
