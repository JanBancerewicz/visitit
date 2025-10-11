package com.example.visitit.spring.dto.room;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomCreateDTO {
    @NotBlank(message = "Room name is required")
    private String name;
}
