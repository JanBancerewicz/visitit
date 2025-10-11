package com.example.visitit.se.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO implements Comparable<ReservationDTO>, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String clientFullName;
    // zamiast employee, trzymamy tylko interesujace nas pola:
    private String employeeFullName;
    private String serviceName;
    private String roomName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String status;

    @Override
    public int compareTo(ReservationDTO o) {
        if (this.startDatetime != null && o.startDatetime != null) {
            return this.startDatetime.compareTo(o.startDatetime);
        }
        if (this.id != null && o.id != null) return this.id.compareTo(o.id);
        return 0;
    }

    @Override
    public String toString() {
        return String.format("ReservationDTO{id=%d, client=%s, employee=%s, service=%s, start=%s}",
                id, clientFullName, employeeFullName, serviceName, startDatetime);
    }
}
