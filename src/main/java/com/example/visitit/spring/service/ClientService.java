package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Client;
import com.example.visitit.spring.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }
        return clientRepository.save(client);
    }

    public void delete(Client client) {
        clientRepository.delete(client);
    }
}
