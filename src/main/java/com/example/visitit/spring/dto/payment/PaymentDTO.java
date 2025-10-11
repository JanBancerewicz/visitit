package com.example.visitit.spring.dto.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private UUID id;
    private UUID reservationId;
    private BigDecimal amount;
    private String status;
    private String method;
    private LocalDateTime paymentDate;
}
