package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByStatus(String status);
    List<Reservation> findByClient_Id(UUID clientId);
    List<Reservation> findByEmployee_Id(UUID employeeId);
    List<Reservation> findByService_Id(UUID serviceId);
    List<Reservation> findByRoom_Id(UUID roomId);

    @Query("""
           select r from Reservation r
           left join fetch r.client
           left join fetch r.employee
           left join fetch r.service
           left join fetch r.room
           where r.service.id = :serviceId
           """)
    List<Reservation> findAllByServiceFetched(@Param("serviceId") UUID serviceId);

    @Query("""
           select r from Reservation r
           left join fetch r.client
           left join fetch r.employee
           left join fetch r.service
           left join fetch r.room
           """)
    List<Reservation> findAllFetched();

    @Query("select r.id from Reservation r where r.client.id = :id")
    List<UUID> findIdsByClientId(@Param("id") UUID id);
    @Query("select r.id from Reservation r where r.employee.id = :id")
    List<UUID> findIdsByEmployeeId(@Param("id") UUID id);
    @Query("select r.id from Reservation r where r.service.id = :id")
    List<UUID> findIdsByServiceId(@Param("id") UUID id);
    @Query("select r.id from Reservation r where r.room.id = :id")
    List<UUID> findIdsByRoomId(@Param("id") UUID id);

    // --- nowa metoda sprawdzająca czy istnieje rezerwacja w tym pokoju, która nakłada się na podany przedział
    @Query("""
           select case when count(r) > 0 then true else false end
           from Reservation r
           where r.room.id = :roomId
             and r.startDatetime < :end
             and r.endDatetime   > :start
           """)
    boolean existsOverlapForRoom(@Param("roomId") UUID roomId,
                                 @Param("start") LocalDateTime start,
                                 @Param("end") LocalDateTime end);
}
