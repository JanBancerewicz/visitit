package com.example.visitit.spring.dto.reservation;

import jakarta.validation.constraints.NotNull;
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
public class ReservationCreateDTO {
    @NotNull(message = "Client ID is required")
    private UUID clientId;

    @NotNull(message = "Employee ID is required")
    private UUID employeeId;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotNull(message = "Room ID is required")
    private UUID roomId;

    @NotNull(message = "Start datetime is required")
    private LocalDateTime startDatetime;

    @NotNull(message = "End datetime is required")
    private LocalDateTime endDatetime;

    private String status;
    private String note;
}
