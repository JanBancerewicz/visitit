package com.example.visitit.se.model;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Room implements Comparable<Room>, Serializable {
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private Long id;
    private String name;

    @Override
    public int compareTo(Room o) {
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
        return String.format("Room{id=%d, name=%s}", id, name);
    }
}
