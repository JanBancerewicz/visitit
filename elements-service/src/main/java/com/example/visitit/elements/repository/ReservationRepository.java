package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    // status: jeśli masz enum, zmień typ parametru
    List<Reservation> findByStatus(String status);

    // UŻYWANE przez ReservationService
    List<Reservation> findByClient_Id(UUID clientId);
    List<Reservation> findByEmployee_Id(UUID employeeId);
    List<Reservation> findByService_Id(UUID serviceId);
    List<Reservation> findByRoom_Id(UUID roomId);

    // (opcjonalnie) możesz zostawić aliasy findAllBy*, ale nie są potrzebne,
    // usuń je jeśli nie są nigdzie użyte, żeby uniknąć duplikatów.

    // Projekcje ID do kaskadowego kasowania (JPQL po relacjach!)
    @Query("select r.id from Reservation r where r.client.id = :id")
    List<UUID> findIdsByClientId(@Param("id") UUID id);

    @Query("select r.id from Reservation r where r.employee.id = :id")
    List<UUID> findIdsByEmployeeId(@Param("id") UUID id);

    @Query("select r.id from Reservation r where r.service.id = :id")
    List<UUID> findIdsByServiceId(@Param("id") UUID id);

    @Query("select r.id from Reservation r where r.room.id = :id")
    List<UUID> findIdsByRoomId(@Param("id") UUID id);
}
