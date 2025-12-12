package com.example.visitit.elements.service;

import com.example.visitit.elements.dto.reservation.*;
import com.example.visitit.elements.model.*;
import com.example.visitit.elements.model.ref.*;
import com.example.visitit.elements.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepo;
    private final ClientRefRepository clientRepo;
    private final EmployeeRefRepository employeeRepo;
    private final ServiceRefRepository serviceRepo;
    private final RoomRefRepository roomRepo;

    @Transactional(readOnly = true)
    public List<ReservationListDTO> list(){
        return reservationRepo.findAllFetched()
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<ReservationDTO> get(UUID id){
        return reservationRepo.findById(id).map(this::toDTO);
    }

    @Transactional
    public Optional<ReservationDTO> create(ReservationCreateDTO dto){
        var client = clientRepo.findById(dto.getClientId()).orElse(null);
        var employee = employeeRepo.findById(dto.getEmployeeId()).orElse(null);
        var service  = serviceRepo.findById(dto.getServiceId()).orElse(null);
        var room     = roomRepo.findById(dto.getRoomId()).orElse(null);
        if (client==null || employee==null || service==null || room==null) return Optional.empty();

        // walidacja czasu
        LocalDateTime start = dto.getStartDatetime();
        LocalDateTime end   = dto.getEndDatetime();
        if (start == null || end == null || !start.isBefore(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid time range");
        }

        // sprawdź kolizję w tym samym pokoju
        boolean overlap = reservationRepo.existsOverlapForRoom(room.getId(), start, end);
        if (overlap) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "time slot already booked");
        }

        var r = Reservation.builder()
                .id(null) // <- pozwól JPA wygenerować UUID
                .client(client).employee(employee).service(service).room(room)
                .startDatetime(start).endDatetime(end)
                .status(dto.getStatus()).note(dto.getNote())
                .build();

        try {
            var saved = reservationRepo.save(r);
            return Optional.of(toDTO(saved));
        } catch (DataIntegrityViolationException ex) {
            // mapuj naruszenia constraints na 409 z użytecznym message
            throw new ResponseStatusException(HttpStatus.CONFLICT, "database constraint: " + ex.getMostSpecificCause().getMessage(), ex);
        }
    }

    @Transactional
    public Optional<ReservationDTO> update(UUID id, ReservationCreateDTO dto){
        return reservationRepo.findById(id).map(ex -> {
            if (dto.getClientId()!=null)   ex.setClient(clientRepo.findById(dto.getClientId()).orElseThrow());
            if (dto.getEmployeeId()!=null) ex.setEmployee(employeeRepo.findById(dto.getEmployeeId()).orElseThrow());
            if (dto.getServiceId()!=null)  ex.setService(serviceRepo.findById(dto.getServiceId()).orElseThrow());
            if (dto.getRoomId()!=null)     ex.setRoom(roomRepo.findById(dto.getRoomId()).orElseThrow());
            if (dto.getStartDatetime()!=null) ex.setStartDatetime(dto.getStartDatetime());
            if (dto.getEndDatetime()!=null)   ex.setEndDatetime(dto.getEndDatetime());
            if (dto.getStatus()!=null)        ex.setStatus(dto.getStatus());
            if (dto.getNote()!=null)          ex.setNote(dto.getNote());
            // opcjonalnie: przy update też warto sprawdzić overlap (z pominięciem aktualnego id)
            // TODO: optionally add overlap check for updates

            return toDTO(reservationRepo.save(ex));
        });
    }

    @Transactional public boolean delete(UUID id){
        return reservationRepo.findById(id).map(r->{ reservationRepo.delete(r); return true; }).orElse(false);
    }

    @Transactional(readOnly = true)
    public List<ReservationListDTO> byStatus(String status){
        return reservationRepo.findByStatus(status).stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationListDTO> byClient(UUID clientId){
        return reservationRepo.findByClient_Id(clientId).stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationListDTO> byEmployee(UUID employeeId){
        return reservationRepo.findByEmployee_Id(employeeId).stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationListDTO> byService(UUID serviceId){
        return reservationRepo.findAllByServiceFetched(serviceId)
                .stream().map(this::toListDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReservationListDTO> byRoom(UUID roomId){
        return reservationRepo.findByRoom_Id(roomId).stream().map(this::toListDTO).collect(Collectors.toList());
    }

    private ReservationDTO toDTO(Reservation r){
        return ReservationDTO.builder()
                .id(r.getId())
                .clientId(r.getClient().getId())
                .employeeId(r.getEmployee().getId())
                .serviceId(r.getService().getId())
                .roomId(r.getRoom().getId())
                .startDatetime(r.getStartDatetime())
                .endDatetime(r.getEndDatetime())
                .status(r.getStatus())
                .note(r.getNote())
                .build();
    }

    private ReservationListDTO toListDTO(Reservation r){
        return ReservationListDTO.builder()
                .id(r.getId())
                .clientName(r.getClient().getDisplayName())
                .employeeName(r.getEmployee().getDisplayName())
                .serviceName(r.getService().getName())
                .roomName(r.getRoom().getName())
                .status(r.getStatus())
                .build();
    }
}
