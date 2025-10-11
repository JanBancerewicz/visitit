package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.employee.EmployeeCreateDTO;
import com.example.visitit.spring.dto.employee.EmployeeDTO;
import com.example.visitit.spring.dto.employee.EmployeeListDTO;
import com.example.visitit.spring.model.Employee;
import com.example.visitit.spring.service.EmployeeService;
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
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final ReservationService reservationService;

    public EmployeeController(EmployeeService employeeService, ReservationService reservationService) {
        this.employeeService = employeeService;
        this.reservationService = reservationService;
    }

    // GET /api/employees - lista wszystkich pracowników
    @GetMapping
    public List<EmployeeListDTO> getAllEmployees() {
        return employeeService.findAll()
                .stream()
                .map(e -> EmployeeListDTO.builder()
                        .id(e.getId())
                        .firstName(e.getFirstName())
                        .lastName(e.getLastName())
                        .role(e.getRole())
                        .build())
                .collect(Collectors.toList());
    }

    // GET /api/employees/{id} - pobranie pojedynczego pracownika
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable UUID id) {
        return employeeService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/employees - tworzenie nowego pracownika
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeCreateDTO dto) {
        Employee employee = Employee.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(dto.getRole())
                .description(dto.getDescription())
                .build();
        Employee saved = employeeService.save(employee);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    // PUT /api/employees/{id} - aktualizacja pracownika
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable UUID id,
                                                      @Valid @RequestBody EmployeeCreateDTO dto) {
        return employeeService.findById(id)
                .map(employee -> {
                    if (dto.getFirstName() != null) employee.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) employee.setLastName(dto.getLastName());
                    if (dto.getRole() != null) employee.setRole(dto.getRole());
                    if (dto.getDescription() != null) employee.setDescription(dto.getDescription());
                    Employee updated = employeeService.save(employee);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/employees/{id} - usuwanie pracownika
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        return employeeService.findById(id)
                .map(employee -> {
                    employeeService.delete(employee);
                    return ResponseEntity.<Void>noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // konwerter entity -> DTO
    private EmployeeDTO toDTO(Employee e) {
        return EmployeeDTO.builder()
                .id(e.getId())
                .firstName(e.getFirstName())
                .lastName(e.getLastName())
                .role(e.getRole())
                .description(e.getDescription())
                .serviceIds(e.getServices() != null
                        ? e.getServices().stream().map(s -> s.getId()).toList()
                        : null)
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
    public ResponseEntity<List<ReservationListDTO>> getEmployeeReservations(@PathVariable UUID id) {
        return employeeService.findById(id)
                .map(e -> reservationService.findByEmployee(id).stream().map(this::toListDTO).collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


}
