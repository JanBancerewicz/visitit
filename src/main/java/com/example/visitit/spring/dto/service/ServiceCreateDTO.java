package com.example.visitit.spring.dto.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCreateDTO {
    @NotBlank(message = "Service name is required")
    private String name;

    private String description;

    @NotNull(message = "Duration is required")
    private Integer durationMin;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}
