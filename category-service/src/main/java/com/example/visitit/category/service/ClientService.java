package com.example.visitit.category.service;

import com.example.visitit.category.model.Client;
import com.example.visitit.category.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repo;

    public List<Client> findAll() {
        return repo.findAll();
    }

    public Optional<Client> findById(UUID id) {
        return repo.findById(id);
    }

    @Transactional
    public Client create(Client in) {
        // wymuś INSERT – ID ma wygenerować JPA (@GeneratedValue)
        in.setId(null);

        if (repo.existsByEmail(in.getEmail())) {
            throw new IllegalStateException("EMAIL_CONFLICT");
        }

        return repo.save(in);
    }

    @Transactional
    public Optional<Client> update(UUID id, Client in) {
        return repo.findById(id).map(existing -> {
            if (repo.existsByEmailAndIdNot(in.getEmail(), id)) {
                throw new IllegalStateException("EMAIL_CONFLICT");
            }

            existing.setFirstName(in.getFirstName());
            existing.setLastName(in.getLastName());
            existing.setEmail(in.getEmail());
            existing.setPhone(in.getPhone());

            return repo.save(existing);
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        return repo.findById(id)
                .map(entity -> {
                    repo.delete(entity);
                    return true;
                })
                .orElse(false);
    }
}
