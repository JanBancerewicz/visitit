package com.example.visitit.elements.model.ref;

import jakarta.persistence.*; import lombok.*; import java.util.UUID;

@Entity @Table(name="room_ref")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RoomRef {
    @Id private UUID id;
    @Column(nullable=false) private String name;
}
