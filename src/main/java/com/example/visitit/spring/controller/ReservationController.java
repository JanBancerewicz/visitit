package com.example.visitit.spring.controller;



import com.example.visitit.spring.dto.reservation.ReservationCreateDTO;
import com.example.visitit.spring.dto.reservation.ReservationDTO;
import com.example.visitit.spring.dto.reservation.ReservationListDTO;
import com.example.visitit.spring.model.Reservation;
import com.example.visitit.spring.service.ClientService;
import com.example.visitit.spring.service.EmployeeService;
import com.example.visitit.spring.service.ReservationService;
import com.example.visitit.spring.service.RoomService;
import com.example.visitit.spring.service.ServiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClientService clientService;
    private final EmployeeService employeeService;
    private final ServiceService serviceService;
    private final RoomService roomService;

    public ReservationController(ReservationService reservationService,
                                 ClientService clientService,
                                 EmployeeService employeeService,
                                 ServiceService serviceService,
                                 RoomService roomService) {
        this.reservationService = reservationService;
        this.clientService = clientService;
        this.employeeService = employeeService;
        this.serviceService = serviceService;
        this.roomService = roomService;
    }

    // GET /api/reservations - lista wszystkich rezerwacji
    @GetMapping
    public List<ReservationListDTO> getAllReservations() {
        return reservationService.findAll()
                .stream()
                .map(r -> ReservationListDTO.builder()
                        .id(r.getId())
                        .clientName(r.getClient().getFirstName() + " " + r.getClient().getLastName())
                        .employeeName(r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName())
                        .serviceName(r.getService().getName())
                        .roomName(r.getRoom().getName())
                        .status(r.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    // GET /api/reservations/{id} - pojedyncza rezerwacja
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservation(@PathVariable UUID id) {
        return reservationService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/reservations - tworzenie rezerwacji
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@Valid @RequestBody ReservationCreateDTO dto) {
        return clientService.findById(dto.getClientId())
                .flatMap(client -> employeeService.findById(dto.getEmployeeId())
                        .flatMap(employee -> serviceService.findById(dto.getServiceId())
                                .flatMap(service -> roomService.findById(dto.getRoomId())
                                        .map(room -> {
                                            Reservation res = Reservation.builder()
                                                    .client(client)
                                                    .employee(employee)
                                                    .service(service)
                                                    .room(room)
                                                    .startDatetime(dto.getStartDatetime())
                                                    .endDatetime(dto.getEndDatetime())
                                                    .status(dto.getStatus())
                                                    .note(dto.getNote())
                                                    .build();
                                            Reservation saved = reservationService.save(res);
                                            return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
                                        }))))
                .orElse(ResponseEntity.badRequest().build());
    }

    // PUT /api/reservations/{id} - aktualizacja rezerwacji
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(@PathVariable UUID id,
                                                            @Valid @RequestBody ReservationCreateDTO dto) {
        return reservationService.findById(id)
                .map(res -> {
                    clientService.findById(dto.getClientId()).ifPresent(res::setClient);
                    employeeService.findById(dto.getEmployeeId()).ifPresent(res::setEmployee);
                    serviceService.findById(dto.getServiceId()).ifPresent(res::setService);
                    roomService.findById(dto.getRoomId()).ifPresent(res::setRoom);
                    if (dto.getStartDatetime() != null) res.setStartDatetime(dto.getStartDatetime());
                    if (dto.getEndDatetime() != null) res.setEndDatetime(dto.getEndDatetime());
                    if (dto.getStatus() != null) res.setStatus(dto.getStatus());
                    if (dto.getNote() != null) res.setNote(dto.getNote());
                    Reservation updated = reservationService.save(res);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/reservations/{id} - usuwanie rezerwacji
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable UUID id) {
        return reservationService.findById(id)
                .map(res -> {
                    reservationService.delete(res);
                    return ResponseEntity.<Void>noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // konwerter entity -> DTO
    private ReservationDTO toDTO(Reservation r) {
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

    // GET /api/reservations?status=CONFIRMED
    @GetMapping(params = "status")
    public List<ReservationListDTO> getByStatus(@RequestParam String status) {
        return reservationService.findByStatus(status).stream()
                .map(this::toListDTO).collect(Collectors.toList());
    }

    @GetMapping("/by-client/{clientId}")
    public ResponseEntity<List<ReservationListDTO>> getByClient(@PathVariable UUID clientId) {
        return clientService.findById(clientId)
                .map(c -> reservationService.findByClient(clientId).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-employee/{employeeId}")
    public ResponseEntity<List<ReservationListDTO>> getByEmployee(@PathVariable UUID employeeId) {
        return employeeService.findById(employeeId)
                .map(e -> reservationService.findByEmployee(employeeId).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-service/{serviceId}")
    public ResponseEntity<List<ReservationListDTO>> getByService(@PathVariable UUID serviceId) {
        return serviceService.findById(serviceId)
                .map(s -> reservationService.findByService(serviceId).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-room/{roomId}")
    public ResponseEntity<List<ReservationListDTO>> getByRoom(@PathVariable UUID roomId) {
        return roomService.findById(roomId)
                .map(r -> reservationService.findByRoom(roomId).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
