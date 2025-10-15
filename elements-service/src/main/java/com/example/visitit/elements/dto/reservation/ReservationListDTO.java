package com.example.visitit.elements.dto.reservation;

import lombok.*; import java.util.UUID;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class ReservationListDTO {
    private UUID id;
    private String clientName;
    private String employeeName;
    private String serviceName;
    private String roomName;
    private String status;
}
