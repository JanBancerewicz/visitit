package com.example.visitit.elements.controller.internal;

import com.example.visitit.elements.service.sync.SyncApplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/internal-sync/categories")
@RequiredArgsConstructor
public class InternalSyncCategoriesController {

    private final SyncApplyService sync;

    @PostMapping("/{type}")
    public ResponseEntity<Void> upsert(@PathVariable String type, @RequestBody Map<String, Object> body) {
        sync.upsert(type, body);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<Void> remove(@PathVariable String type, @PathVariable UUID id) {
        sync.remove(type, id);
        return ResponseEntity.noContent().build();
    }
}
