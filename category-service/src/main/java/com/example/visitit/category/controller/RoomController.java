package com.example.visitit.category.controller;

import com.example.visitit.category.model.Room;
import com.example.visitit.category.service.RoomService;
import com.example.visitit.category.sync.SyncPublisher;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService service;
    private final SyncPublisher sync;

    @GetMapping public List<Room> list(){ return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Room> get(@PathVariable UUID id){
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Room> create(@Valid @RequestBody Room body){
        Room saved = service.create(body);
        sync.pushRoom(saved.getId(), saved.getName());
        return ResponseEntity.created(URI.create("/api/rooms/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable UUID id, @Valid @RequestBody Room body){
        return service.update(id, body).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
