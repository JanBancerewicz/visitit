package com.example.visitit.elements.model.ref;

import jakarta.persistence.*; import lombok.*; import java.util.UUID;

@Entity @Table(name="service_ref")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceRef {
    @Id private UUID id;
    @Column(nullable=false) private String name;
}
