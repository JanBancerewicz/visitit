package com.example.visitit.elements.service;

import com.example.visitit.elements.model.Reservation;
import com.example.visitit.elements.model.ref.*;
import com.example.visitit.elements.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List; import java.util.UUID;

@Service @RequiredArgsConstructor
public class RefSyncService {
    private final ClientRefRepository clientRepo;  private final EmployeeRefRepository employeeRepo;
    private final ServiceRefRepository serviceRepo; private final RoomRefRepository roomRepo;
    private final ReservationRepository reservationRepo; private final PaymentRepository paymentRepo;

    @Transactional public void upsertClient(UUID id, String displayName){
        clientRepo.save(ClientRef.builder().id(id).displayName(displayName).build());
    }
    @Transactional public void upsertEmployee(UUID id, String displayName){
        employeeRepo.save(EmployeeRef.builder().id(id).displayName(displayName).build());
    }
    @Transactional public void upsertService(UUID id, String name){
        serviceRepo.save(ServiceRef.builder().id(id).name(name).build());
    }
    @Transactional public void upsertRoom(UUID id, String name){
        roomRepo.save(RoomRef.builder().id(id).name(name).build());
    }

    @Transactional public void deleteClient(UUID id){ cleanup(reservationRepo.findAllByClient_Id(id)); clientRepo.deleteById(id); }
    @Transactional public void deleteEmployee(UUID id){ cleanup(reservationRepo.findAllByEmployee_Id(id)); employeeRepo.deleteById(id); }
    @Transactional public void deleteService(UUID id){ cleanup(reservationRepo.findAllByService_Id(id)); serviceRepo.deleteById(id); }
    @Transactional public void deleteRoom(UUID id){ cleanup(reservationRepo.findAllByRoom_Id(id)); roomRepo.deleteById(id); }

    private void cleanup(List<Reservation> reservations){
        for (var r : reservations) { paymentRepo.deleteByReservation_Id(r.getId()); }
        reservationRepo.deleteAll(reservations);
    }
}
