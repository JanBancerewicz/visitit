package com.example.visitit.spring.model;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "services")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employees")
public class Service implements Serializable {
    @Id
    private UUID id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_min")
    private int durationMin;

    private BigDecimal price;

    @ManyToMany(mappedBy = "services")
    @Builder.Default
    private List<Employee> employees = new ArrayList<>();
}
