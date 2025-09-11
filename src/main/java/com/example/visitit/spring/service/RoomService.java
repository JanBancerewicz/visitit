package com.example.visitit.spring.service;

import com.example.visitit.spring.model.Room;
import com.example.visitit.spring.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    public Optional<Room> findById(UUID id) {
        return roomRepository.findById(id);
    }

    public Room save(Room room) {
        if (room.getId() == null) {
            room.setId(UUID.randomUUID());
        }
        return roomRepository.save(room);
    }

    public void delete(Room room) {
        roomRepository.delete(room);
    }

    public void deleteById(UUID id) {
        roomRepository.deleteById(id);
    }
}
