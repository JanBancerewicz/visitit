package com.example.visitit.spring.dto.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO {
    private UUID id;
    private UUID clientId;
    private UUID employeeId;
    private UUID serviceId;
    private UUID roomId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String status;
    private String note;
}
