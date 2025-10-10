package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.payment.PaymentCreateDTO;
import com.example.visitit.spring.dto.payment.PaymentDTO;
import com.example.visitit.spring.model.Payment;
import com.example.visitit.spring.service.PaymentService;
import com.example.visitit.spring.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final ReservationService reservationService;

    // GET all payments
    @GetMapping
    public List<PaymentDTO> getAllPayments() {
        return paymentService.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // GET payment by ID
    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable UUID id) {
        return paymentService.findById(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create payment
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentCreateDTO dto) {
        return reservationService.findById(dto.getReservationId())
                .map(reservation -> {
                    Payment payment = Payment.builder()
                            .reservation(reservation)
                            .amount(dto.getAmount())
                            .status(dto.getStatus())
                            .method(dto.getMethod())
                            .paymentDate(dto.getPaymentDate())
                            .build();
                    Payment saved = paymentService.save(payment);
                    return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    // PUT update payment
    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable UUID id,
                                                    @Valid @RequestBody PaymentCreateDTO dto) {
        return paymentService.findById(id)
                .map(payment -> {
                    reservationService.findById(dto.getReservationId())
                            .ifPresent(payment::setReservation);
                    if (dto.getAmount() != null) payment.setAmount(dto.getAmount());
                    if (dto.getStatus() != null) payment.setStatus(dto.getStatus());
                    if (dto.getMethod() != null) payment.setMethod(dto.getMethod());
                    if (dto.getPaymentDate() != null) payment.setPaymentDate(dto.getPaymentDate());
                    Payment updated = paymentService.save(payment);
                    return ResponseEntity.ok(toDTO(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE payment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable UUID id) {
        return paymentService.findById(id)
                .map(payment -> {
                    paymentService.delete(payment);
                    return ResponseEntity.noContent().<Void>build();h
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Helper: convert Payment -> PaymentDTO
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
