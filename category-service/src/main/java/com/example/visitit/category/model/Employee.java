package com.example.visitit.category.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "employee")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Employee {
    @Id private UUID id;

    @NotBlank @Column(nullable = false) private String firstName;
    @NotBlank @Column(nullable = false) private String lastName;
    @NotBlank @Column(nullable = false) private String role;
    private String description;
}
