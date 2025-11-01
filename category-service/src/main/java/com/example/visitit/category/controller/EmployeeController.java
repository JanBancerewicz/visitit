package com.example.visitit.category.controller;

import com.example.visitit.category.model.Employee;
import com.example.visitit.category.service.EmployeeService;
import com.example.visitit.category.sync.SyncPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService service;
    private final SyncPublisher sync;

    @GetMapping public List<Employee> list(){ return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> get(@PathVariable UUID id){
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee body){
        Employee saved = service.create(body);
        sync.pushEmployee(saved.getId(), saved.getFirstName() + " " + saved.getLastName());
        return ResponseEntity.created(URI.create("/api/employees/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> update(@PathVariable UUID id, @Valid @RequestBody Employee body){
        return service.update(id, body).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
