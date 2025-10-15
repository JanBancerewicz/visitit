package com.example.visitit.elements.service;

import com.example.visitit.elements.dto.payment.*;
import com.example.visitit.elements.model.Payment;
import com.example.visitit.elements.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*; import java.util.stream.Collectors; import java.util.UUID;

@Service @RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final ReservationRepository reservationRepo;

    public List<PaymentDTO> list(){
        return paymentRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    public Optional<PaymentDTO> get(UUID id){ return paymentRepo.findById(id).map(this::toDTO); }
    public Optional<PaymentDTO> findByReservation(UUID reservationId){
        return paymentRepo.findByReservation_Id(reservationId).map(this::toDTO);
    }
    public boolean existsForReservation(UUID reservationId){
        return paymentRepo.existsByReservation_Id(reservationId);
    }

    @Transactional
    public Optional<PaymentDTO> createFlat(PaymentCreateDTO dto){
        var res = reservationRepo.findById(dto.getReservationId()).orElse(null);
        if (res==null) return Optional.empty();
        if (existsForReservation(res.getId())) throw new IllegalStateException("PAYMENT_CONFLICT");
        var p = Payment.builder()
                .id(UUID.randomUUID()).reservation(res)
                .amount(dto.getAmount()).status(dto.getStatus()).method(dto.getMethod()).paymentDate(dto.getPaymentDate())
                .build();
        return Optional.of(toDTO(paymentRepo.save(p)));
    }

    @Transactional
    public Optional<PaymentDTO> createForReservation(UUID reservationId, PaymentCreateDTO dto){
        var res = reservationRepo.findById(reservationId).orElse(null);
        if (res==null) return Optional.empty();
        if (existsForReservation(reservationId)) throw new IllegalStateException("PAYMENT_CONFLICT");
        var p = Payment.builder()
                .id(UUID.randomUUID()).reservation(res)
                .amount(dto.getAmount()).status(dto.getStatus()).method(dto.getMethod()).paymentDate(dto.getPaymentDate())
                .build();
        return Optional.of(toDTO(paymentRepo.save(p)));
    }

    @Transactional
    public Optional<PaymentDTO> update(UUID id, PaymentCreateDTO dto){
        return paymentRepo.findById(id).map(ex -> {
            if (dto.getAmount()!=null) ex.setAmount(dto.getAmount());
            if (dto.getStatus()!=null) ex.setStatus(dto.getStatus());
            if (dto.getMethod()!=null) ex.setMethod(dto.getMethod());
            if (dto.getPaymentDate()!=null) ex.setPaymentDate(dto.getPaymentDate());
            return toDTO(paymentRepo.save(ex));
        });
    }

    @Transactional public boolean delete(UUID id){
        return paymentRepo.findById(id).map(p->{ paymentRepo.delete(p); return true; }).orElse(false);
    }

    private PaymentDTO toDTO(Payment p){
        return PaymentDTO.builder()
                .id(p.getId())
                .reservationId(p.getReservation().getId())
                .amount(p.getAmount())
                .status(p.getStatus())
                .method(p.getMethod())
                .paymentDate(p.getPaymentDate())
                .build();
    }
}
