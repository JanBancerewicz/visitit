package com.example.visitit.spring.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String role;
    private String description;
    private List<UUID> serviceIds; // tylko identyfikatory usług
}
