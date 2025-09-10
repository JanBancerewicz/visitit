package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Reservation;
import com.example.visitit.spring.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public Reservation findById(UUID id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> findByClient(UUID clientId) {
        return reservationRepository.findByClientId(clientId);
    }

    public List<Reservation> findByEmployee(UUID employeeId) {
        return reservationRepository.findByEmployeeId(employeeId);
    }

    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }
}
