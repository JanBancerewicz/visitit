package com.example.visitit.category.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "service", uniqueConstraints = @UniqueConstraint(name = "uk_service_name", columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @NotBlank @Column(nullable = false, unique = true) private String name;
    private String description;

    @Positive @Column(nullable = false) private Integer durationMin;
    @DecimalMin("0.0") @Column(nullable = false, precision = 10, scale = 2) private BigDecimal price;
}
