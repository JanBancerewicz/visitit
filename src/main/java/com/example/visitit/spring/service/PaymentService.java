package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Payment;
import com.example.visitit.spring.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> findById(UUID id) {
        return paymentRepository.findById(id);
    }

    public Payment save(Payment payment) {
        if (payment.getId() == null) {
            payment.setId(UUID.randomUUID());
        }
        return paymentRepository.save(payment);
    }

    public void delete(Payment payment) {
        paymentRepository.delete(payment);
    }

    public void deleteById(UUID id) {
        paymentRepository.deleteById(id);
    }
}
