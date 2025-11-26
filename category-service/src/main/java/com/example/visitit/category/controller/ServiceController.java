package com.example.visitit.category.controller;

import com.example.visitit.category.model.ServiceEntity;
import com.example.visitit.category.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceService service;

    @GetMapping
    public List<ServiceEntity> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ServiceEntity get(@PathVariable UUID id) {
        return service.get(id);
    }

    @PostMapping
    public ResponseEntity<ServiceEntity> create(@RequestBody ServiceEntity body) {
        var saved = service.create(body);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ServiceEntity update(@PathVariable UUID id, @RequestBody ServiceEntity body) {
        return service.update(id, body);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        service.delete(id);
    }
}
