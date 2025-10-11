package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByStatus(String status);
    List<Reservation> findByClientId(UUID clientId);
    List<Reservation> findByEmployeeId(UUID employeeId);
    List<Reservation> findByServiceId(UUID serviceId);
    List<Reservation> findByRoomId(UUID roomId);
}
