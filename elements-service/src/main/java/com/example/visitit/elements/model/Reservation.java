package com.example.visitit.elements.model;

import com.example.visitit.elements.model.ref.*;
import jakarta.persistence.*; import lombok.*;
import java.time.LocalDateTime; import java.util.UUID;

@Entity @Table(name="reservation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Reservation {
    @Id private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="client_id", nullable=false)
    private ClientRef client;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="employee_id", nullable=false)
    private EmployeeRef employee;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="service_id", nullable=false)
    private ServiceRef service;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="room_id", nullable=false)
    private RoomRef room;

    @Column(name="start_datetime", nullable=false) private LocalDateTime startDatetime;
    @Column(name="end_datetime",   nullable=false) private LocalDateTime endDatetime;
    @Column(name="status",         nullable=false) private String status;
    @Column(name="note") private String note;
}
