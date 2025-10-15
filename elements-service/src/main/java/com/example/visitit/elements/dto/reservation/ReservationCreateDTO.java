package com.example.visitit.elements.dto.reservation;

import lombok.Getter; import lombok.Setter;
import java.time.LocalDateTime; import java.util.UUID;

@Getter @Setter
public class ReservationCreateDTO {
    private UUID clientId;
    private UUID employeeId;
    private UUID serviceId;
    private UUID roomId;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String status;
    private String note;
}
