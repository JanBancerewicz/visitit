package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Reservation;
import com.example.visitit.spring.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            reservation.setId(UUID.randomUUID());
        }
        return reservationRepository.save(reservation);
    }

    public void delete(Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public void deleteById(UUID id) {
        reservationRepository.deleteById(id);
    }

    public Optional<Reservation> findById(UUID id) {
        return reservationRepository.findById(id);
    }


    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }
    public List<Reservation> findByClient(UUID clientId) {
        return reservationRepository.findByClientId(clientId);
    }
    public List<Reservation> findByEmployee(UUID employeeId) {
        return reservationRepository.findByEmployeeId(employeeId);
    }
    public List<Reservation> findByService(UUID serviceId) {
        return reservationRepository.findByServiceId(serviceId);
    }
    public List<Reservation> findByRoom(UUID roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

}
