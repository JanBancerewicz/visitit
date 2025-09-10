package com.example.visitit.spring.repository;

import com.example.visitit.spring.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    // przykładowe wyszukiwanie po nazwisku
    // List<Client> findByLastName(String lastName);
}
