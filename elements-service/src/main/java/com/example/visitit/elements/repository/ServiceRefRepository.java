package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.ref.ServiceRef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ServiceRefRepository extends JpaRepository<ServiceRef, UUID> {}
