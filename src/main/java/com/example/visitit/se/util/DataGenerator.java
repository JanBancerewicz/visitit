package com.example.visitit.se.util;

import com.example.visitit.se.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;


public class DataGenerator {
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    private static final List<String> FIRST_NAMES = List.of(
            "John", "Martin", "Sam", "Michał", "Kacper", "Adam", "Jessica", "Paulina", "Justyna", "Zofia"
    );
    private static final List<String> LAST_NAMES = List.of(
            "Wójcik", "Nowak", "Bancerewicz", "Lewandowski", "Doe", "Smith", "Jones", "Miller", "Brown", "Walker"
    );
    private static final List<String> ROLES = List.of(
            "Specjalista", "Terapeuta", "Masażysta", "Psycholog", "Dietetyk", "Trener", "Lekarz", "Recepcjonista", "Asystent", "Konsultant"
    );
    private static final List<String> DESCRIPTIONS = List.of(
            "Doświadczony specjalista", "Fachowiec w swojej dziedzinie", "Empatyczny i profesjonalny", "Młody i ambitny",
            "Zawsze punktualny", "Komunikatywny", "Ceniony przez klientów", "Skuteczny i cierpliwy", "Kreatywny terapeuta", "Lubi swoją pracę"
    );
    private static final List<String> SERVICE_NAMES = List.of(
            "Konsultacja", "Terapia", "Masaż", "Badanie", "Diagnostyka", "Kontrola", "Warsztat", "Porada", "Rehabilitacja", "Coaching"
    );
    private static final List<BigDecimal> SERVICE_PRICES = List.of(
            new BigDecimal("50.00"), new BigDecimal("80.00"), new BigDecimal("100.00"),
            new BigDecimal("120.00"), new BigDecimal("150.00"), new BigDecimal("200.00"),
            new BigDecimal("75.00"), new BigDecimal("95.00"), new BigDecimal("130.00"), new BigDecimal("300.00")
    );
    private static final List<String> ROOM_NAMES = List.of(
            "Gabinet A", "Gabinet B", "Gabinet C", "Sala 1", "Sala 2", "Pokój relaksacyjny", "Laboratorium", "Studio", "Pokój konsultacyjny", "Gabinet VIP"
    );
    private static final List<String> STATUSES = List.of(
            "CONFIRMED", "PENDING", "CANCELLED", "RESCHEDULED", "COMPLETED"
    );

    private static final Random RANDOM = new Random();

    public static Long nextId() {
        return ID_GEN.getAndIncrement();
    }

    private static String randomElement(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    private static BigDecimal randomPrice() {
        return SERVICE_PRICES.get(RANDOM.nextInt(SERVICE_PRICES.size()));
    }

    public static List<Employee> sampleEmployeesWithReservations(int clientCount, int serviceCount, int employeeCount, int reservationCount) {
        List<Employee> employees = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();

        // Utwórz pulę klientów
        for (int i = 0; i < clientCount; i++) {
            clients.add(Client.builder()
                    .id(nextId())
                    .firstName(randomElement(FIRST_NAMES))
                    .lastName(randomElement(LAST_NAMES))
                    .email("client" + i + "@example.com")
                    .phone("600" + (100000 + RANDOM.nextInt(899999)))
                    .registrationDate(LocalDateTime.now().minusDays(RANDOM.nextInt(60)))
                    .build());
        }

        // Utwórz pulę usług
        for (int i = 0; i < serviceCount; i++) {
            services.add(Service.builder()
                    .id(nextId())
                    .name(randomElement(SERVICE_NAMES))
                    .description("Opis: " + randomElement(DESCRIPTIONS))
                    .durationMin(30 + RANDOM.nextInt(60))
                    .price(randomPrice())
                    .build());
        }

        // Utwórz pokoje (wszystkie z predefiniowanej listy)
        for (String name : ROOM_NAMES) {
            rooms.add(Room.builder().id(nextId()).name(name).build());
        }

        // Utwórz pracowników
        for (int i = 0; i < employeeCount; i++) {
            Employee e = Employee.builder()
                    .id(nextId())
                    .firstName(randomElement(FIRST_NAMES))
                    .lastName(randomElement(LAST_NAMES))
                    .role(randomElement(ROLES))
                    .description(randomElement(DESCRIPTIONS))
                    .build();
            employees.add(e);
        }

        // Utwórz rezerwacje
        for (int i = 0; i < reservationCount; i++) {
            Client c = clients.get(RANDOM.nextInt(clients.size()));
            Employee e = employees.get(RANDOM.nextInt(employees.size()));
            Service s = services.get(RANDOM.nextInt(services.size()));
            Room r = rooms.get(RANDOM.nextInt(rooms.size()));

            LocalDateTime start = LocalDateTime.now().plusDays(RANDOM.nextInt(30)).withHour(8 + RANDOM.nextInt(10))
                    .withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = start.plusMinutes(s.getDurationMin());

            Reservation res = Reservation.builder()
                    .id(nextId())
                    .client(c)
                    .employee(e)
                    .service(s)
                    .room(r)
                    .startDatetime(start)
                    .endDatetime(end)
                    .status(randomElement(STATUSES))
                    .note(RANDOM.nextBoolean() ? "Notatka: " + randomElement(DESCRIPTIONS) : null)
                    .build();

            e.getReservations().add(res);
        }

        return employees;
    }
}
