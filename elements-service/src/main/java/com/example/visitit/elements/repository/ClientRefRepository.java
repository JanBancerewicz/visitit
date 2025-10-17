package com.example.visitit.elements.repository;

import com.example.visitit.elements.model.ref.ClientRef;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ClientRefRepository extends JpaRepository<ClientRef, UUID> {}
