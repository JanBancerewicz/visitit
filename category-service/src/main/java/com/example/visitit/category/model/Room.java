package com.example.visitit.category.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "room", uniqueConstraints = @UniqueConstraint(name = "uk_room_name", columnNames = "name"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {
    @Id private UUID id;

    @NotBlank @Column(nullable = false, unique = true) private String name;
}
