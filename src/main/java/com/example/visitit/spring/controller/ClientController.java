package com.example.visitit.spring.controller;

import com.example.visitit.spring.dto.client.ClientCreateDTO;
import com.example.visitit.spring.dto.client.ClientDTO;
import com.example.visitit.spring.dto.client.ClientListDTO;
import com.example.visitit.spring.model.Client;
import com.example.visitit.spring.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // GET /api/clients - lista wszystkich klientów (tylko podstawowe dane)
    @GetMapping
    public ResponseEntity<List<ClientListDTO>> getAllClients() {
        List<ClientListDTO> list = clientService.findAll()
                .stream()
                .map(client -> ClientListDTO.builder()
                        .id(client.getId())
                        .firstName(client.getFirstName())
                        .lastName(client.getLastName())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // GET /api/clients/{id} - pobranie pojedynczego klienta
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable UUID id) {
        return clientService.findById(id)
                .map(client -> ClientDTO.builder()
                        .id(client.getId())
                        .firstName(client.getFirstName())
                        .lastName(client.getLastName())
                        .email(client.getEmail())
                        .phone(client.getPhone())
                        .registrationDate(client.getRegistrationDate())
                        .build())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/clients - tworzenie nowego klienta
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientCreateDTO dto) {
        Client client = Client.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .registrationDate(java.time.LocalDateTime.now())
                .build();
        clientService.save(client);

        ClientDTO response = ClientDTO.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .phone(client.getPhone())
                .registrationDate(client.getRegistrationDate())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/clients/{id} - aktualizacja klienta
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable UUID id, @Valid @RequestBody ClientCreateDTO dto) {
        return clientService.findById(id)
                .map(client -> {
                    if (dto.getFirstName() != null) client.setFirstName(dto.getFirstName());
                    if (dto.getLastName() != null) client.setLastName(dto.getLastName());
                    if (dto.getEmail() != null) client.setEmail(dto.getEmail());
                    if (dto.getPhone() != null) client.setPhone(dto.getPhone());
                    clientService.save(client);

                    ClientDTO response = ClientDTO.builder()
                            .id(client.getId())
                            .firstName(client.getFirstName())
                            .lastName(client.getLastName())
                            .email(client.getEmail())
                            .phone(client.getPhone())
                            .registrationDate(client.getRegistrationDate())
                            .build();
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/clients/{id} - usuwanie klienta
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        return clientService.findById(id)
                .map(client -> {
                    clientService.delete(client);
                    return ResponseEntity.noContent().<Void>build(); // <- jawnie typujemy na Void
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
