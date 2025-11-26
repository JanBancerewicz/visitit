package com.example.visitit.category.service;

import com.example.visitit.category.model.ServiceEntity;
import com.example.visitit.category.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {

    private final ServiceRepository repo;

    @Transactional(readOnly = true)
    public List<ServiceEntity> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public ServiceEntity get(UUID id) {
        return repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
    }

    public ServiceEntity create(ServiceEntity body) {
        // KLUCZOWE: wymuś persist zamiast merge (fix 500)
        body.setId(null);

        if (body.getName() == null || body.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (repo.existsByNameIgnoreCase(body.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name must be unique");
        }
        return repo.save(body);
    }

    public ServiceEntity update(UUID id, ServiceEntity body) {
        var entity = repo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));

        var newName = body.getName();
        if (newName == null || newName.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (repo.existsByNameIgnoreCaseAndIdNot(newName, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "name must be unique");
        }

        entity.setName(newName);
        entity.setDescription(body.getDescription());
        entity.setDurationMin(body.getDurationMin());
        entity.setPrice(body.getPrice());

        return repo.save(entity);
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found");
        }
        repo.deleteById(id);
    }
}
