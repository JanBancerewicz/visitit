package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByReservationId(UUID reservationId);
    boolean existsByReservationId(UUID reservationId);
}
