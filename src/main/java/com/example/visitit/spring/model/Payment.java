package com.example.visitit.spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "reservations")
public class Payment implements Serializable {
    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    private BigDecimal amount;

    private String status;

    private String method;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
}
