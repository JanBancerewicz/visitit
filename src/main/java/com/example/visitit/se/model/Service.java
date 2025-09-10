package com.example.visitit.se.model;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Service implements Comparable<Service>, Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private String description;
    private int durationMin;
    private BigDecimal price;

    @Override
    public int compareTo(Service o) {
        if (this.id != null && o.id != null) return this.id.compareTo(o.id);
        return compareNullable(this.name, o.name);
    }

    private static int compareNullable(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    @Override
    public String toString() {
        return String.format("Service{id=%d, name=%s, duration=%dmin}", id, name, durationMin);
    }
}
