package com.example.visitit.elements.model;

import jakarta.persistence.*; import lombok.*;
import java.math.BigDecimal; import java.time.LocalDateTime; import java.util.UUID;

@Entity
@Table(name="payment",
        uniqueConstraints = @UniqueConstraint(name="uk_payment_reservation", columnNames="reservation_id"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name="reservation_id", nullable=false)
    private Reservation reservation;

    @Column(nullable=false, precision=10, scale=2) private BigDecimal amount;
    @Column(nullable=false) private String status;
    @Column(nullable=false) private String method;
    @Column(name="payment_date", nullable=false) private LocalDateTime paymentDate;
}
