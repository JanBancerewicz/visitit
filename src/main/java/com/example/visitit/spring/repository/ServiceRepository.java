package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Service, UUID> {
    // metody typu findByCategory jeśli będziesz miał kategorię
}
