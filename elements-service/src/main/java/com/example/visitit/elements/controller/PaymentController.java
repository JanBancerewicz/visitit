package com.example.visitit.elements.controller;

import com.example.visitit.elements.dto.payment.*;
import com.example.visitit.elements.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*; import org.springframework.web.bind.annotation.*;

import java.net.URI; import java.util.*; import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @GetMapping("/payments")
    public List<PaymentDTO> list(){ return service.list(); }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> get(@PathVariable UUID id){
        return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/payments")
    public ResponseEntity<?> createFlat(@RequestBody PaymentCreateDTO dto){
        try {
            var created = service.createFlat(dto);
            if (created.isEmpty()) return ResponseEntity.badRequest().build();
            var body = created.get();
            return ResponseEntity.created(URI.create("/api/payments/" + body.getId())).body(body);
        } catch (IllegalStateException e){
            if ("PAYMENT_CONFLICT".equals(e.getMessage())) return ResponseEntity.status(HttpStatus.CONFLICT).build();
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable UUID id, @RequestBody PaymentCreateDTO dto){
        return service.update(id, dto).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        return service.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentDTO> getByReservation(@PathVariable UUID reservationId){
        var found = service.findByReservation(reservationId);
        return found.map(ResponseEntity::ok).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PostMapping("/reservations/{reservationId}/payment")
    public ResponseEntity<?> createForReservation(@PathVariable UUID reservationId, @RequestBody PaymentCreateDTO dto){
        try {
            var created = service.createForReservation(reservationId, dto);
            if (created.isEmpty()) return ResponseEntity.notFound().build();
            var body = created.get();
            return ResponseEntity.created(URI.create("/api/payments/" + body.getId())).body(body);
        } catch (IllegalStateException e){
            if ("PAYMENT_CONFLICT".equals(e.getMessage())) return ResponseEntity.status(HttpStatus.CONFLICT).build();
            return ResponseEntity.badRequest().build();
        }
    }
}
