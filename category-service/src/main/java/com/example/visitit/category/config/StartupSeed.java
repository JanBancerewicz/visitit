package com.example.visitit.category.config;

import com.example.visitit.category.model.Client;
import com.example.visitit.category.model.Employee;
import com.example.visitit.category.model.Room;
import com.example.visitit.category.model.ServiceEntity;
import com.example.visitit.category.repository.ClientRepository;
import com.example.visitit.category.repository.EmployeeRepository;
import com.example.visitit.category.repository.RoomRepository;
import com.example.visitit.category.repository.ServiceRepository;
import com.example.visitit.category.sync.SyncPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class StartupSeed {

    private final ServiceRepository serviceRepo;
    private final ClientRepository clientRepo;
    private final EmployeeRepository employeeRepo;
    private final RoomRepository roomRepo;
    private final SyncPublisher sync;

    @Bean
    ApplicationRunner seedAndSync() {
        return args -> {
            // ===== Services =====
            var service = serviceRepo.findAll().stream()
                    .filter(s -> "Strzyżenie".equalsIgnoreCase(s.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        var s = new ServiceEntity();
                        // Jeśli używasz @GeneratedValue – USUŃ tę linię:
                        s.setId(UUID.randomUUID());
                        s.setName("Strzyżenie");
                        s.setDescription("Klasyczne");
                        s.setDurationMin(60);
                        s.setPrice(BigDecimal.valueOf(89.99));
                        return serviceRepo.save(s);
                    });
            sync.pushService(service.getId(), service.getName());

            // ===== Client =====
            var client = clientRepo.findAll().stream()
                    .filter(c -> "jan.kowalski@example.com".equalsIgnoreCase(c.getEmail()))
                    .findFirst()
                    .orElseGet(() -> {
                        var c = new Client();
                        // Jeśli używasz @GeneratedValue – USUŃ tę linię:
                        c.setId(UUID.randomUUID());
                        c.setFirstName("Jan");
                        c.setLastName("Kowalski");
                        c.setEmail("jan.kowalski@example.com");
                        c.setPhone("123456789");
                        return clientRepo.save(c);
                    });
            sync.pushClient(client.getId(), client.getFirstName() + " " + client.getLastName());

            // ===== Employee =====
            var employee = employeeRepo.findAll().stream()
                    .filter(e -> "Anna".equalsIgnoreCase(e.getFirstName())
                            && "Nowak".equalsIgnoreCase(e.getLastName())
                            && "Fryzjer".equalsIgnoreCase(e.getRole()))
                    .findFirst()
                    .orElseGet(() -> {
                        var e = new Employee();
                        // Jeśli używasz @GeneratedValue – USUŃ tę linię:
                        e.setId(UUID.randomUUID());
                        e.setFirstName("Anna");
                        e.setLastName("Nowak");
                        e.setRole("Fryzjer");
                        e.setDescription("Senior");
                        return employeeRepo.save(e);
                    });
            sync.pushEmployee(employee.getId(), employee.getFirstName() + " " + employee.getLastName());

            // ===== Room =====
            var room = roomRepo.findAll().stream()
                    .filter(r -> "Sala 1".equalsIgnoreCase(r.getName()))
                    .findFirst()
                    .orElseGet(() -> {
                        var r = new Room();
                        // Jeśli używasz @GeneratedValue – USUŃ tę linię:
                        r.setId(UUID.randomUUID());
                        r.setName("Sala 1");
                        return roomRepo.save(r);
                    });
            sync.pushRoom(room.getId(), room.getName());
        };
    }

    /**
     * DODANE: drugi runner, który przez ~15s ponawia synchronizację referencji.
     * Dzięki temu kolejność startu usług nie ma znaczenia:
     * - jeśli elements-service nie działał przy seedzie, referencje i tak dojadą, gdy tylko wstanie.
     * - wywołania są idempotentne (po stronie elements możesz zwrócić 409/200 – to OK).
     */
    @Bean
    ApplicationRunner resyncRefsWithRetry() {
        return args -> {
            // 30 prób co 500 ms ≈ 15 sekund
            for (int i = 0; i < 30; i++) {
                try {
                    // Wyciągamy bieżące rekordy z bazy (to co zasialiśmy wyżej)
                    var serviceOpt = serviceRepo.findAll().stream()
                            .filter(s -> "Strzyżenie".equalsIgnoreCase(s.getName()))
                            .findFirst();
                    var clientOpt = clientRepo.findAll().stream()
                            .filter(c -> "jan.kowalski@example.com".equalsIgnoreCase(c.getEmail()))
                            .findFirst();
                    var employeeOpt = employeeRepo.findAll().stream()
                            .filter(e -> "Anna".equalsIgnoreCase(e.getFirstName())
                                    && "Nowak".equalsIgnoreCase(e.getLastName())
                                    && "Fryzjer".equalsIgnoreCase(e.getRole()))
                            .findFirst();
                    var roomOpt = roomRepo.findAll().stream()
                            .filter(r -> "Sala 1".equalsIgnoreCase(r.getName()))
                            .findFirst();

                    if (serviceOpt.isPresent()) {
                        var s = serviceOpt.get();
                        sync.pushService(s.getId(), s.getName());
                    }
                    if (clientOpt.isPresent()) {
                        var c = clientOpt.get();
                        sync.pushClient(c.getId(), c.getFirstName() + " " + c.getLastName());
                    }
                    if (employeeOpt.isPresent()) {
                        var e = employeeOpt.get();
                        sync.pushEmployee(e.getId(), e.getFirstName() + " " + e.getLastName());
                    }
                    if (roomOpt.isPresent()) {
                        var r = roomOpt.get();
                        sync.pushRoom(r.getId(), r.getName());
                    }

                    Thread.sleep(500);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        };
    }
}
