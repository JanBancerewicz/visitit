package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByClientId(UUID clientId);
    List<Reservation> findByEmployeeId(UUID employeeId);
    List<Reservation> findByStatus(String status);
}
