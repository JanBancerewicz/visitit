package com.example.visitit.elements.controller;

import com.example.visitit.elements.service.RefSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map; import java.util.UUID;

@RestController
@RequestMapping("/internal-sync/categories")
@RequiredArgsConstructor
public class InternalSyncController {
    private final RefSyncService refSync;

    @PostMapping("/{type}")
    public ResponseEntity<Void> upsert(@PathVariable String type, @RequestBody Map<String,Object> body){
        var id = UUID.fromString((String) body.get("id"));
        switch (type) {
            case "client"   -> refSync.upsertClient(id,   (String) body.get("displayName"));
            case "employee" -> refSync.upsertEmployee(id, (String) body.get("displayName"));
            case "service"  -> refSync.upsertService(id,  (String) body.get("name"));
            case "room"     -> refSync.upsertRoom(id,     (String) body.get("name"));
            default -> { return ResponseEntity.badRequest().build(); }
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> delete(@PathVariable String type, @PathVariable UUID id){
        switch (type) {
            case "client" -> refSync.deleteClient(id);
            case "employee" -> refSync.deleteEmployee(id);
            case "service" -> refSync.deleteService(id);
            case "room" -> refSync.deleteRoom(id);
            default -> { return ResponseEntity.badRequest().build(); }
        }
        return ResponseEntity.noContent().build();
    }
}
