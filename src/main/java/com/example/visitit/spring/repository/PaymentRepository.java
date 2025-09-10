package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    // Możesz tu dodać np. wyszukiwanie po statusie lub metodzie płatności
}
