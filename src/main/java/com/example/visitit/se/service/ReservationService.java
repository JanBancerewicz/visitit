package com.example.visitit.se.service;

import com.example.visitit.se.model.Reservation;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Prosty in-memory CRUD dla Reservation. Thread-safe-ish via ConcurrentHashMap.
 */
public class ReservationService implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<Long, Reservation> storage = new ConcurrentHashMap<>();

    // Create or update
    public Reservation save(Reservation reservation) {
        if (reservation == null || reservation.getId() == null) {
            throw new IllegalArgumentException("Reservation or id is null");
        }
        storage.put(reservation.getId(), reservation);
        return reservation;
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<Reservation> findAll() {
        return new ArrayList<>(storage.values());
    }

    public List<Reservation> findByEmployeeId(Long employeeId) {
        return storage.values().stream()
                .filter(r -> r.getEmployee() != null && employeeId.equals(r.getEmployee().getId()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByClientId(Long clientId) {
        return storage.values().stream()
                .filter(r -> r.getClient() != null && clientId.equals(r.getClient().getId()))
                .collect(Collectors.toList());
    }

    public List<Reservation> findByDateRange(LocalDateTime from, LocalDateTime to) {
        return storage.values().stream()
                .filter(r -> {
                    LocalDateTime s = r.getStartDatetime();
                    return s != null && (s.isEqual(from) || s.isAfter(from)) && (s.isEqual(to) || s.isBefore(to));
                })
                .collect(Collectors.toList());
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }

    public void clear() {
        storage.clear();
    }

    // Serialization helpers for persistence of all reservations
    public void saveToFile(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new ArrayList<>(storage.values()));
        }
    }

    @SuppressWarnings("unchecked")
    public List<Reservation> loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Reservation> list = (List<Reservation>) ois.readObject();
            storage.clear();
            list.forEach(r -> storage.put(r.getId(), r));
            return list;
        }
    }
}
