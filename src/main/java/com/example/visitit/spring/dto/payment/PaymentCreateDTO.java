package com.example.visitit.spring.dto.payment;

import jakarta.validation.constraints.NotNull;
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
public class PaymentCreateDTO {
    @NotNull(message = "Reservation ID is required")
    private UUID reservationId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String status;
    private String method;
    private LocalDateTime paymentDate;
}
