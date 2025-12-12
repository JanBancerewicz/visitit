package com.example.visitit.category.service;

import com.example.visitit.category.model.Room;
import com.example.visitit.category.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repo;

    public List<Room> findAll() {
        return repo.findAll();
    }

    public Optional<Room> findById(UUID id) {
        return repo.findById(id);
    }

    @Transactional
    public Room create(Room in) {
        // wymuś INSERT – ID ma wygenerować JPA (@GeneratedValue)
        in.setId(null);

        if (repo.existsByName(in.getName())) {
            throw new IllegalStateException("NAME_CONFLICT");
        }

        return repo.save(in);
    }

    @Transactional
    public Optional<Room> update(UUID id, Room in) {
        return repo.findById(id).map(existing -> {
            if (repo.existsByNameAndIdNot(in.getName(), id)) {
                throw new IllegalStateException("NAME_CONFLICT");
            }
            existing.setName(in.getName());
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
