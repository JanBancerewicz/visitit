package com.example.visitit.elements.dto.payment;

import lombok.Getter; import lombok.Setter;
import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.UUID;

@Getter @Setter
public class PaymentCreateDTO {
    private UUID reservationId; // dla POST /api/payments
    private BigDecimal amount;
    private String status;
    private String method;
    private LocalDateTime paymentDate;
}
