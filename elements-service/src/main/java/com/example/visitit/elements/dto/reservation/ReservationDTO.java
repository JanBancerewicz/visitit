package com.example.visitit.elements.dto.reservation;

import lombok.*; import java.time.LocalDateTime; import java.util.UUID;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
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
