package com.example.visitit.spring.dto.reservation;

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
public class ReservationUpdateDTO {
    private UUID clientId;
    private UUID employeeId;
    private UUID serviceId;
    private UUID roomId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String status;
    private String note;
}
