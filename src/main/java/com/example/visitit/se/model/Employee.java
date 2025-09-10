package com.example.visitit.se.model;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Employee implements Comparable<Employee>, Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private String role;
    private String description;

    // 1:N relationship: one Employee -> many Reservations
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public int compareTo(Employee o) {
        if (this.id != null && o.id != null) return this.id.compareTo(o.id);
        int cmp = compareNullable(this.lastName, o.lastName);
        if (cmp != 0) return cmp;
        return compareNullable(this.firstName, o.firstName);
    }

    private static int compareNullable(String a, String b) {
        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;
        return a.compareTo(b);
    }

    @Override
    public String toString() {
        return String.format("Employee{id=%d, %s %s, role=%s}", id, firstName, lastName, role);
    }
}
