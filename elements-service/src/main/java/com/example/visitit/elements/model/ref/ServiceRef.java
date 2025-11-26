package com.example.visitit.elements.model.ref;

import jakarta.persistence.*; import lombok.*; import java.util.UUID;

@Entity @Table(name="service_ref")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceRef {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;
    @Column(nullable=false) private String name;
}
