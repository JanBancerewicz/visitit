package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*; import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    Optional<Payment> findByReservation_Id(UUID reservationId);
    boolean existsByReservation_Id(UUID reservationId);

    void deleteByReservation_Id(UUID reservationId);

}
