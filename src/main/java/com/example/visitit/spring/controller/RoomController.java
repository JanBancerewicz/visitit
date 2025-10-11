package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.room.RoomCreateDTO;
import com.example.visitit.spring.dto.room.RoomDTO;
import com.example.visitit.spring.dto.room.RoomListDTO;
import com.example.visitit.spring.model.Room;
import com.example.visitit.spring.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.visitit.spring.dto.reservation.ReservationListDTO;
import com.example.visitit.spring.model.Reservation;
import com.example.visitit.spring.service.ReservationService;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    private final ReservationService reservationService;


    public RoomController(RoomService roomService, ReservationService reservationService) {
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    // GET /api/rooms - lista wszystkich pokoi
    @GetMapping
    public List<RoomListDTO> getAllRooms() {
        return roomService.findAll()
                .stream()
                .map(r -> RoomListDTO.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .build())
                .collect(Collectors.toList());
    }

    // GET /api/rooms/{id} - pojedynczy pokój
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoom(@PathVariable UUID id) {
        return roomService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/rooms - tworzenie pokoju
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(@Valid @RequestBody RoomCreateDTO dto) {
        Room room = Room.builder()
                .name(dto.getName())
                .build();
        Room saved = roomService.save(room);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    // PUT /api/rooms/{id} - aktualizacja pokoju
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable UUID id,
                                              @Valid @RequestBody RoomCreateDTO dto) {
        return roomService.findById(id)
                .map(room -> {
                    if (dto.getName() != null) room.setName(dto.getName());
                    Room updated = roomService.save(room);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/rooms/{id} - usuwanie pokoju
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable UUID id) {
        return roomService.findById(id)
                .map(room -> {
                    roomService.delete(room);
                    return ResponseEntity.<Void>noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // konwerter entity -> DTO
    private RoomDTO toDTO(Room r) {
        return RoomDTO.builder()
                .id(r.getId())
                .name(r.getName())
                .build();
    }

    private ReservationListDTO toListDTO(Reservation r) {
        return ReservationListDTO.builder()
                .id(r.getId())
                .clientName(r.getClient().getFirstName() + " " + r.getClient().getLastName())
                .employeeName(r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName())
                .serviceName(r.getService().getName())
                .roomName(r.getRoom().getName())
                .status(r.getStatus())
                .build();
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationListDTO>> getRoomReservations(@PathVariable UUID id) {
        return roomService.findById(id)
                .map(r -> reservationService.findByRoom(id).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
