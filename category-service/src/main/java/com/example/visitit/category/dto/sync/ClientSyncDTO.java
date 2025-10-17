package com.example.visitit.category.dto.sync;

import java.util.UUID;

public record ClientSyncDTO(UUID id, String displayName, String email) {}