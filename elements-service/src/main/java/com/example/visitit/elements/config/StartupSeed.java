package com.example.visitit.elements.config;

import com.example.visitit.elements.model.Reservation;
import com.example.visitit.elements.repository.ClientRefRepository;
import com.example.visitit.elements.repository.EmployeeRefRepository;
import com.example.visitit.elements.repository.ReservationRepository;
import com.example.visitit.elements.repository.RoomRefRepository;
import com.example.visitit.elements.repository.ServiceRefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class StartupSeed {

    private final ReservationRepository reservationRepo;
    private final ClientRefRepository clientRefRepo;
    private final EmployeeRefRepository employeeRefRepo;
    private final ServiceRefRepository serviceRefRepo;
    private final RoomRefRepository roomRefRepo;

    @Bean
    ApplicationRunner seedReservationWhenRefsReady() {
        return args -> {
            if (reservationRepo.count() > 0) return;

            int attempts = 60; // ~30s (60 x 500ms)
            while (attempts-- > 0) {
                boolean ready =
                        clientRefRepo.count()   > 0 &&
                                employeeRefRepo.count() > 0 &&
                                serviceRefRepo.count()  > 0 &&
                                roomRefRepo.count()     > 0;
                if (ready) {
                    var client   = clientRefRepo.findAll().get(0);
                    var employee = employeeRefRepo.findAll().get(0);
                    var service  = serviceRefRepo.findAll().get(0);
                    var room     = roomRefRepo.findAll().get(0);

                    var start = LocalDateTime.now().withSecond(0).withNano(0).plusHours(1);
                    var end   = start.plusHours(1);

                    var r = Reservation.builder()
                            .client(client)
                            .employee(employee)
                            .service(service)
                            .room(room)
                            .startDatetime(start)
                            .endDatetime(end)
                            .status("PLANNED")
                            .note("Seed (elements waits for refs)")
                            .build();

                    r.setId(java.util.UUID.randomUUID());

                    reservationRepo.save(r);
                    System.out.println("[SEED/elements] Reservation created.");
                    return;
                }
                try { Thread.sleep(500); } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt(); return;
                }
            }
            System.out.println("[SEED/elements] No refs after timeout — skipping.");
        };
    }
}
