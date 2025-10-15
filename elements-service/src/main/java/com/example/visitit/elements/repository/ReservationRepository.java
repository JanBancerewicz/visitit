package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*; import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByStatus(String status);
    List<Reservation> findByClient_Id(UUID clientId);
    List<Reservation> findByEmployee_Id(UUID employeeId);
    List<Reservation> findByService_Id(UUID serviceId);
    List<Reservation> findByRoom_Id(UUID roomId);

    List<Reservation> findAllByClient_Id(UUID clientId);
    List<Reservation> findAllByEmployee_Id(UUID employeeId);
    List<Reservation> findAllByService_Id(UUID serviceId);
    List<Reservation> findAllByRoom_Id(UUID roomId);
}
