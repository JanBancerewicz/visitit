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

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
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
}
