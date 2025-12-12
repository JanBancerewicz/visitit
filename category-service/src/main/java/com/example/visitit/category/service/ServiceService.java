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
    public ServiceEntity findById(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service not found"));
    }

    // kompatybilna metoda oczekiwana przez ServiceController.get(...)
    @Transactional(readOnly = true)
    public ServiceEntity get(UUID id) {
        return findById(id);
    }

    public ServiceEntity create(ServiceEntity body) {
        body.setId(null);

        validate(body);

        if (repo.existsByNameIgnoreCase(body.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Service with this name already exists");
        }

        return repo.save(body);
    }

    public ServiceEntity update(UUID id, ServiceEntity body) {
        ServiceEntity entity = findById(id);

        validate(body);

        if (repo.existsByNameIgnoreCaseAndIdNot(body.getName(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Service with this name already exists");
        }

        entity.setName(body.getName());
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

    private void validate(ServiceEntity body) {
        if (body.getName() == null || body.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
        }
        if (body.getDurationMin() == null || body.getDurationMin() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "durationMin must be positive");
        }
        if (body.getPrice() == null || body.getPrice().signum() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "price must be non-negative");
        }
    }
}
