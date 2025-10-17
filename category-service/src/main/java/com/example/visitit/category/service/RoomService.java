package com.example.visitit.category.service;

import com.example.visitit.category.model.Room;
import com.example.visitit.category.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service @RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repo;

    public List<Room> findAll(){ return repo.findAll(); }
    public Optional<Room> findById(UUID id){ return repo.findById(id); }

    @Transactional
    public Room create(Room in){
        if(in.getId()==null) in.setId(UUID.randomUUID());
        if(repo.existsByName(in.getName())) throw new IllegalStateException("NAME_CONFLICT");
        return repo.save(in);
    }

    @Transactional
    public Optional<Room> update(UUID id, Room in){
        return repo.findById(id).map(ex -> {
            if(repo.existsByNameAndIdNot(in.getName(), id)) throw new IllegalStateException("NAME_CONFLICT");
            ex.setName(in.getName());
            return repo.save(ex);
        });
    }

    @Transactional
    public boolean delete(UUID id){
        return repo.findById(id).map(e->{ repo.delete(e); return true; }).orElse(false);
    }
}
