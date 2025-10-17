package com.example.visitit.elements.service;

import com.example.visitit.elements.model.ref.ClientRef;
import com.example.visitit.elements.model.ref.EmployeeRef;
import com.example.visitit.elements.model.ref.RoomRef;
import com.example.visitit.elements.model.ref.ServiceRef;
import com.example.visitit.elements.repository.PaymentRepository;
import com.example.visitit.elements.repository.ReservationRepository;
import com.example.visitit.elements.repository.ClientRefRepository;
import com.example.visitit.elements.repository.EmployeeRefRepository;
import com.example.visitit.elements.repository.RoomRefRepository;
import com.example.visitit.elements.repository.ServiceRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefSyncService {

    private final ClientRefRepository clientRepo;
    private final EmployeeRefRepository employeeRepo;
    private final ServiceRefRepository serviceRepo;
    private final RoomRefRepository roomRepo;

    private final ReservationRepository reservationRepo;
    private final PaymentRepository paymentRepo;

    /* ----------------- UPSERT ----------------- */

    @Transactional
    public void upsertClient(UUID id, String displayName) {
        clientRepo.save(ClientRef.builder()
                .id(id)
                .displayName(displayName)
                .build());
    }

    @Transactional
    public void upsertEmployee(UUID id, String displayName) {
        employeeRepo.save(EmployeeRef.builder()
                .id(id)
                .displayName(displayName)
                .build());
    }

    @Transactional
    public void upsertService(UUID id, String name) {
        serviceRepo.save(ServiceRef.builder()
                .id(id)
                .name(name)
                .build());
    }

    @Transactional
    public void upsertRoom(UUID id, String name) {
        roomRepo.save(RoomRef.builder()
                .id(id)
                .name(name)
                .build());
    }

    /* ----------------- DELETE + CLEANUP ----------------- */

    @Transactional
    public void deleteClient(UUID id) {
        List<UUID> resIds = reservationRepo.findIdsByClientId(id);
        cascadeDeleteReservations(resIds);
        clientRepo.deleteById(id);
    }

    @Transactional
    public void deleteEmployee(UUID id) {
        List<UUID> resIds = reservationRepo.findIdsByEmployeeId(id);
        cascadeDeleteReservations(resIds);
        employeeRepo.deleteById(id);
    }

    @Transactional
    public void deleteService(UUID id) {
        List<UUID> resIds = reservationRepo.findIdsByServiceId(id);
        cascadeDeleteReservations(resIds);
        serviceRepo.deleteById(id);
    }

    @Transactional
    public void deleteRoom(UUID id) {
        List<UUID> resIds = reservationRepo.findIdsByRoomId(id);
        cascadeDeleteReservations(resIds);
        roomRepo.deleteById(id);
    }

    private void cascadeDeleteReservations(List<UUID> resIds) {
        if (resIds == null || resIds.isEmpty()) return;
        paymentRepo.deleteByReservationIdIn(resIds);
        reservationRepo.deleteAllById(resIds);
    }
}
