package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.service.ServiceCreateDTO;
import com.example.visitit.spring.dto.service.ServiceDTO;
import com.example.visitit.spring.dto.service.ServiceListDTO;
import com.example.visitit.spring.model.Service;
import com.example.visitit.spring.service.ServiceService;
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
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService serviceService;

    private final ReservationService reservationService;


    public ServiceController(ServiceService serviceService, ReservationService reservationService) {
        this.serviceService = serviceService;
        this.reservationService = reservationService;
    }

    // GET /api/services - lista wszystkich usług
    @GetMapping
    public List<ServiceListDTO> getAllServices() {
        return serviceService.findAll()
                .stream()
                .map(s -> ServiceListDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .durationMin(s.getDurationMin())
                        .price(s.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    // GET /api/services/{id} - pojedyncza usługa
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDTO> getService(@PathVariable UUID id) {
        return serviceService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/services - tworzenie usługi
    @PostMapping
    public ResponseEntity<ServiceDTO> createService(@Valid @RequestBody ServiceCreateDTO dto) {
        Service service = Service.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .durationMin(dto.getDurationMin())
                .price(dto.getPrice())
                .build();
        Service saved = serviceService.save(service);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    // PUT /api/services/{id} - aktualizacja usługi
    @PutMapping("/{id}")
    public ResponseEntity<ServiceDTO> updateService(@PathVariable UUID id,
                                                       @Valid @RequestBody ServiceCreateDTO dto) {
        return serviceService.findById(id)
                .map(service -> {
                    if (dto.getName() != null) service.setName(dto.getName());
                    if (dto.getDescription() != null) service.setDescription(dto.getDescription());
                    if (dto.getDurationMin() != null) service.setDurationMin(dto.getDurationMin());
                    if (dto.getPrice() != null) service.setPrice(dto.getPrice());
                    Service updated = serviceService.save(service);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/services/{id} - usuwanie usługi
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        return serviceService.findById(id)
                .map(service -> {
                    serviceService.delete(service);
                    return ResponseEntity.<Void>noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // konwerter entity -> DTO
    private ServiceDTO toDTO(Service s) {
        return ServiceDTO.builder()
                .id(s.getId())
                .name(s.getName())
                .description(s.getDescription())
                .durationMin(s.getDurationMin())
                .price(s.getPrice())
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
    public ResponseEntity<List<ReservationListDTO>> getServiceReservations(@PathVariable UUID id) {
        return serviceService.findById(id)
                .map(s -> reservationService.findByService(id).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
