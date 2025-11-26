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
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class StartupSeed {

    private final ServiceRepository serviceRepo;
    private final ClientRepository clientRepo;
    private final EmployeeRepository employeeRepo;
    private final RoomRepository roomRepo;
    private final SyncPublisher sync;

    @Bean
    ApplicationRunner seedAndSync() {
        return new ApplicationRunner() {
            @Override @Transactional
            public void run(ApplicationArguments args) {
                // ===== Service (unikat: name) =====
                var service = serviceRepo.findByNameIgnoreCase("Strzyżenie").orElseGet(() -> {
                    var s = ServiceEntity.builder()
                            .name("Strzyżenie")
                            .description("Klasyczne")
                            .durationMin(60)
                            .price(new BigDecimal("89.99"))
                            .build(); // id == null -> @GeneratedValue
                    return serviceRepo.save(s);
                });

                // ===== Client (unikat: email) =====
                var client = clientRepo.findByEmailIgnoreCase("jan.kowalski@example.com").orElseGet(() -> {
                    var c = Client.builder()
                            .firstName("Jan")
                            .lastName("Kowalski")
                            .email("jan.kowalski@example.com")
                            .phone("123456789")
                            .build();
                    return clientRepo.save(c);
                });

                // ===== Employee (unikat: (firstName,lastName,role)) =====
                var employee = employeeRepo
                        .findFirstByFirstNameIgnoreCaseAndLastNameIgnoreCaseAndRoleIgnoreCase("Anna","Nowak","Fryzjer")
                        .orElseGet(() -> {
                            var e = Employee.builder()
                                    .firstName("Anna")
                                    .lastName("Nowak")
                                    .role("Fryzjer")
                                    .description("Senior")
                                    .build();
                            return employeeRepo.save(e);
                        });

                // ===== Room (unikat: name) =====
                var room = roomRepo.findByNameIgnoreCase("Sala 1").orElseGet(() -> {
                    var r = Room.builder().name("Sala 1").build();
                    return roomRepo.save(r);
                });

                // miękki sync – nie zabija startu jeśli elements jest off
                try { sync.pushService(service.getId(), service.getName()); } catch (Exception e) { log.warn("[SYNC] service soft-fail: {}", e.getMessage()); }
                try { sync.pushClient(client.getId(), client.getFirstName() + " " + client.getLastName()); } catch (Exception e) { log.warn("[SYNC] client soft-fail: {}", e.getMessage()); }
                try { sync.pushEmployee(employee.getId(), employee.getFirstName() + " " + employee.getLastName()); } catch (Exception e) { log.warn("[SYNC] employee soft-fail: {}", e.getMessage()); }
                try { sync.pushRoom(room.getId(), room.getName()); } catch (Exception e) { log.warn("[SYNC] room soft-fail: {}", e.getMessage()); }
            }
        };
    }
}
