package com.example.visitit.elements.model.ref;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "room_ref")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomRef {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;              // ID z category-service, bez @GeneratedValue

    @Column(nullable = false)
    private String name;
}
