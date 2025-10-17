package com.example.visitit.elements.controller;

import com.example.visitit.elements.dto.reservation.*;
import com.example.visitit.elements.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;

import java.net.URI; import java.util.*; import java.util.UUID;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService service;

    @GetMapping public List<ReservationListDTO> list(){ return service.list(); }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> get(@PathVariable UUID id){
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ReservationCreateDTO dto){
        var created = service.create(dto);
        if (created.isEmpty()) return ResponseEntity.badRequest().body("Missing refs");
        var body = created.get();
        return ResponseEntity.created(URI.create("/api/reservations/" + body.getId())).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(@PathVariable UUID id, @RequestBody ReservationCreateDTO dto){
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping(params="status")
    public List<ReservationListDTO> byStatus(@RequestParam String status){ return service.byStatus(status); }
    @GetMapping("/by-client/{clientId}")
    public List<ReservationListDTO> byClient(@PathVariable UUID clientId){ return service.byClient(clientId); }
    @GetMapping("/by-employee/{employeeId}")
    public List<ReservationListDTO> byEmployee(@PathVariable UUID employeeId){ return service.byEmployee(employeeId); }
    @GetMapping("/by-service/{serviceId}")
    public List<ReservationListDTO> byService(@PathVariable UUID serviceId){ return service.byService(serviceId); }
    @GetMapping("/by-room/{roomId}")
    public List<ReservationListDTO> byRoom(@PathVariable UUID roomId){ return service.byRoom(roomId); }
}
