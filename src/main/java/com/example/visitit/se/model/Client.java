package com.example.visitit.se.model;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client implements Comparable<Client>, Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime registrationDate;

    @Override
    public int compareTo(Client o) {
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
        return String.format("Client{id=%d, %s %s, email=%s}", id, firstName, lastName, email);
    }
}
