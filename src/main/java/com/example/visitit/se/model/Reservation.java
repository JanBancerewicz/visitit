package com.example.visitit.se.model;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Reservation implements Comparable<Reservation>, Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private Client client;
    private Employee employee;
    private Service service;
    private Room room; // optional
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String status;
    private String note;

    @Override
    public int compareTo(Reservation o) {
        if (this.startDatetime != null && o.startDatetime != null) {
            int cmp = this.startDatetime.compareTo(o.startDatetime);
            if (cmp != 0) return cmp;
        }
        if (this.id != null && o.id != null) return this.id.compareTo(o.id);
        return 0;
    }

    @Override
    public String toString() {
        return String.format("Res{id=%d, client=%s %s, employee=%s %s, service=%s, start=%s, status=%s}",
                id,
                client != null ? client.getFirstName() : "n/a",
                client != null ? client.getLastName() : "",
                employee != null ? employee.getFirstName() : "n/a",
                employee != null ? employee.getLastName() : "",
                service != null ? service.getName() : "n/a",
                startDatetime,
                status);
    }
}
