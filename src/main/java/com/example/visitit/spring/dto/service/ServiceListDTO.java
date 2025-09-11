package com.example.visitit.spring.dto.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceListDTO {
    private UUID id;
    private String name;
    private Integer durationMin;
    private String description;
    private BigDecimal price;
}
