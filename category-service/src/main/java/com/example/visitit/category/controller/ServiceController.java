package com.example.visitit.category.controller;

import com.example.visitit.category.model.ServiceEntity;
import com.example.visitit.category.service.ServiceService;
import com.example.visitit.category.sync.SyncPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService service;
    private final SyncPublisher sync;

    @GetMapping public List<ServiceEntity> list(){ return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceEntity> get(@PathVariable UUID id){
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceEntity> create(@Valid @RequestBody ServiceEntity body){
        ServiceEntity saved = service.create(body);
        sync.pushService(saved.getId(), saved.getName());
        return ResponseEntity.created(URI.create("/api/services/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceEntity> update(@PathVariable UUID id, @Valid @RequestBody ServiceEntity body){
        return service.update(id, body).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
