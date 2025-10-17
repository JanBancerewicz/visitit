package com.example.visitit.elements.dto.payment;

import lombok.*; import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.UUID;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentDTO {
    private UUID id;
    private UUID reservationId;
    private BigDecimal amount;
    private String status;
    private String method;
    private LocalDateTime paymentDate;
}
