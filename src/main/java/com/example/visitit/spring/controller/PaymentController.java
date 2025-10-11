package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.payment.PaymentCreateDTO;
import com.example.visitit.spring.dto.payment.PaymentDTO;
import com.example.visitit.spring.model.Payment;
import com.example.visitit.spring.service.PaymentService;
import com.example.visitit.spring.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    /* ---------- /api/payments ---------- */

    @GetMapping("/payments")
    public List<PaymentDTO> getAllPayments() {
        return paymentService.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable UUID id) {
        return paymentService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/payments")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentCreateDTO dto) {
        if (dto.getReservationId() == null) return ResponseEntity.badRequest().build();
        if (paymentService.existsForReservation(dto.getReservationId()))
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

        return reservationService.findById(dto.getReservationId()).map(res -> {
            Payment payment = Payment.builder()
                    .reservation(res)
                    .amount(dto.getAmount())
                    .status(dto.getStatus())
                    .method(dto.getMethod())
                    .paymentDate(dto.getPaymentDate())
                    .build();
            Payment saved = paymentService.save(payment);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Location", "/api/payments/" + saved.getId())
                    .body(toDTO(saved));
        }).orElse(ResponseEntity.badRequest().build());
    }

    @PutMapping("/payments/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable UUID id,
                                                    @Valid @RequestBody PaymentCreateDTO dto) {
        return paymentService.findById(id).map(existing -> {
            // nie zmieniamy powiązania z rezerwacją w tym wariancie
            if (dto.getAmount() != null) existing.setAmount(dto.getAmount());
            if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
            if (dto.getMethod() != null) existing.setMethod(dto.getMethod());
            if (dto.getPaymentDate() != null) existing.setPaymentDate(dto.getPaymentDate());
            Payment updated = paymentService.save(existing);
            return ResponseEntity.ok(toDTO(updated));
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        return paymentService.findById(id).map(p -> {
            paymentService.delete(p);
            return ResponseEntity.noContent().<Void>build();
        }).orElse(ResponseEntity.notFound().build());
    }

    /* ---------- /api/reservations/{reservationId}/payment ---------- */

    @GetMapping("/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentDTO> getByReservation(@PathVariable UUID reservationId) {
        return reservationService.findById(reservationId)
                .map(r -> paymentService.findByReservation(reservationId)
                        .map(p -> ResponseEntity.ok(toDTO(p)))
                        .orElseGet(() -> new ResponseEntity<PaymentDTO>(HttpStatus.NO_CONTENT)))
                .orElseGet(() -> new ResponseEntity<PaymentDTO>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/reservations/{reservationId}/payment")
    public ResponseEntity<PaymentDTO> createForReservation(@PathVariable UUID reservationId,
                                                           @RequestBody PaymentCreateDTO body) {
        return reservationService.findById(reservationId).map(res -> {
            if (paymentService.existsForReservation(reservationId))
                return ResponseEntity.status(HttpStatus.CONFLICT).<PaymentDTO>build();
            if (body == null || body.getAmount() == null)
                return ResponseEntity.badRequest().<PaymentDTO>build();

            Payment p = Payment.builder()
                    .reservation(res)
                    .amount(body.getAmount())
                    .status(body.getStatus())
                    .method(body.getMethod())
                    .paymentDate(body.getPaymentDate())
                    .build();
            Payment saved = paymentService.save(p);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .header("Location", "/api/payments/" + saved.getId())
                    .body(toDTO(saved));
        }).orElseGet(() -> new ResponseEntity<PaymentDTO>(HttpStatus.NOT_FOUND));
    }

    private PaymentDTO toDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .reservationId(payment.getReservation().getId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .paymentDate(payment.getPaymentDate())
                .build();
    }
}
