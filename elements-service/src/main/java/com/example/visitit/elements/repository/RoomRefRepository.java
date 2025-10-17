package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.ref.RoomRef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RoomRefRepository extends JpaRepository<RoomRef, UUID> {}
