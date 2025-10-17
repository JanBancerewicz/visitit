package com.example.visitit.category.service;

import com.example.visitit.category.model.Client;
import com.example.visitit.category.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repo;

    public List<Client> findAll(){ return repo.findAll(); }
    public Optional<Client> findById(UUID id){ return repo.findById(id); }

    @Transactional
    public Client create(Client in){
        if(in.getId()==null) in.setId(UUID.randomUUID());
        if(repo.existsByEmail(in.getEmail())) throw new IllegalStateException("EMAIL_CONFLICT");
        return repo.save(in);
    }

    @Transactional
    public Optional<Client> update(UUID id, Client in){
        return repo.findById(id).map(ex -> {
            if(repo.existsByEmailAndIdNot(in.getEmail(), id)) throw new IllegalStateException("EMAIL_CONFLICT");
            ex.setFirstName(in.getFirstName());
            ex.setLastName(in.getLastName());
            ex.setEmail(in.getEmail());
            ex.setPhone(in.getPhone());
            return repo.save(ex);
        });
    }

    @Transactional
    public boolean delete(UUID id){
        return repo.findById(id).map(e->{ repo.delete(e); return true; }).orElse(false);
    }
}
