package com.example.visitit.elements.service.sync;

import com.example.visitit.elements.model.ref.ClientRef;
import com.example.visitit.elements.model.ref.EmployeeRef;
import com.example.visitit.elements.model.ref.RoomRef;
import com.example.visitit.elements.model.ref.ServiceRef;
import com.example.visitit.elements.repository.ClientRefRepository;
import com.example.visitit.elements.repository.EmployeeRefRepository;
import com.example.visitit.elements.repository.PaymentRepository;
import com.example.visitit.elements.repository.ReservationRepository;
import com.example.visitit.elements.repository.RoomRefRepository;
import com.example.visitit.elements.repository.ServiceRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncApplyService {

    private final ClientRefRepository clientRefRepo;
    private final EmployeeRefRepository employeeRefRepo;
    private final ServiceRefRepository serviceRefRepo;
    private final RoomRefRepository roomRefRepo;

    private final ReservationRepository reservationRepo;
    private final PaymentRepository paymentRepo;

    @Transactional
    public void upsert(String type, Map<String, Object> body) {
        UUID id = UUID.fromString(String.valueOf(body.get("id")));
        switch (type) {
            case "client"   -> upsertClient(id, (String) body.get("displayName")); // email ignorowany w ref
            case "employee" -> upsertEmployee(id, (String) body.get("displayName"));
            case "service"  -> upsertService(id, (String) body.get("name"));
            case "room"     -> upsertRoom(id, (String) body.get("name"));
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    @Transactional
    public void remove(String type, UUID id) {
        switch (type) {
            case "client" -> { clientRefRepo.deleteById(id); deleteReservationsByClient(id); }
            case "employee" -> { employeeRefRepo.deleteById(id); deleteReservationsByEmployee(id); }
            case "service" -> { serviceRefRepo.deleteById(id); deleteReservationsByService(id); }
            case "room" -> { roomRefRepo.deleteById(id); deleteReservationsByRoom(id); }
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        }
    }

    /* upsert helpers */

    private void upsertClient(UUID id, String displayName) {
        clientRefRepo.save(ClientRef.builder().id(id).displayName(displayName).build());
    }

    private void upsertEmployee(UUID id, String displayName) {
        employeeRefRepo.save(EmployeeRef.builder().id(id).displayName(displayName).build());
    }

    private void upsertService(UUID id, String name) {
        serviceRefRepo.save(ServiceRef.builder().id(id).name(name).build());
    }

    private void upsertRoom(UUID id, String name) {
        roomRefRepo.save(RoomRef.builder().id(id).name(name).build());
    }

    /* cascade delete helpers */

    private void deleteReservationsByClient(UUID clientId) {
        List<UUID> resIds = reservationRepo.findIdsByClientId(clientId);
        cascadeDeleteReservations(resIds);
    }

    private void deleteReservationsByEmployee(UUID employeeId) {
        List<UUID> resIds = reservationRepo.findIdsByEmployeeId(employeeId);
        cascadeDeleteReservations(resIds);
    }

    private void deleteReservationsByService(UUID serviceId) {
        List<UUID> resIds = reservationRepo.findIdsByServiceId(serviceId);
        cascadeDeleteReservations(resIds);
    }

    private void deleteReservationsByRoom(UUID roomId) {
        List<UUID> resIds = reservationRepo.findIdsByRoomId(roomId);
        cascadeDeleteReservations(resIds);
    }

    private void cascadeDeleteReservations(List<UUID> resIds) {
        if (resIds == null || resIds.isEmpty()) return;
        paymentRepo.deleteByReservationIdIn(resIds); // UWAGA: po relacji
        reservationRepo.deleteAllById(resIds);
    }
}
