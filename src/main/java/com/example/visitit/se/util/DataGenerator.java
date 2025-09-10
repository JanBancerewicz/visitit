package com.example.visitit.se.util;

import com.example.visitit.se.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generuje przykładowe dane i wiąże relacje dwukierunkowe.
 */
public class DataGenerator {
    private static final AtomicLong ID_GEN = new AtomicLong(1);

    public static Long nextId() {
        return ID_GEN.getAndIncrement();
    }

    public static List<Employee> sampleEmployeesWithReservations() {
        List<Client> clients = new ArrayList<>();
        clients.add(Client.builder().id(nextId()).firstName("Anna").lastName("Kowalska").email("anna@example.com").phone("600111222").registrationDate(LocalDateTime.now().minusDays(30)).build());
        clients.add(Client.builder().id(nextId()).firstName("Piotr").lastName("Nowak").email("piotr@example.com").phone("600333444").registrationDate(LocalDateTime.now().minusDays(10)).build());
        clients.add(Client.builder().id(nextId()).firstName("Ewa").lastName("Zielinska").email("ewa@example.com").phone("600555666").registrationDate(LocalDateTime.now().minusDays(5)).build());

        List<Service> services = new ArrayList<>();
        services.add(Service.builder().id(nextId()).name("Konsultacja").description("Konsultacja diagnostyczna").durationMin(30).price(new BigDecimal("50.00")).build());
        services.add(Service.builder().id(nextId()).name("Terapia").description("Sesja terapeutyczna").durationMin(60).price(new BigDecimal("120.00")).build());
        services.add(Service.builder().id(nextId()).name("Masaż").description("Relaksacyjny masaż").durationMin(45).price(new BigDecimal("80.00")).build());

        List<Room> rooms = new ArrayList<>();
        rooms.add(Room.builder().id(nextId()).name("Gabinet A").build());
        rooms.add(Room.builder().id(nextId()).name("Gabinet B").build());

        List<Employee> employees = new ArrayList<>();
        Employee e1 = Employee.builder().id(nextId()).firstName("Marta").lastName("Sikora").role("Specjalista").description("Specjalista od terapii").build();
        Employee e2 = Employee.builder().id(nextId()).firstName("Jan").lastName("Lewandowski").role("Terapeuta").description("Doświadczony terapeuta").build();
        Employee e3 = Employee.builder().id(nextId()).firstName("Kasia").lastName("Wójcik").role("Masażysta").description("Fachowiec od masaży").build();

        // create reservations and link both ways
        List<Reservation> res = new ArrayList<>();
        res.add(Reservation.builder()
                .id(nextId())
                .client(clients.get(0))
                .employee(e1)
                .service(services.get(0))
                .room(rooms.get(0))
                .startDatetime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0))
                .endDatetime(LocalDateTime.now().plusDays(1).withHour(10).withMinute(30))
                .status("CONFIRMED")
                .note("Pierwsza wizyta").build());

        res.add(Reservation.builder()
                .id(nextId())
                .client(clients.get(1))
                .employee(e1)
                .service(services.get(1))
                .room(rooms.get(0))
                .startDatetime(LocalDateTime.now().plusDays(2).withHour(12).withMinute(0))
                .endDatetime(LocalDateTime.now().plusDays(2).withHour(13).withMinute(0))
                .status("PENDING")
                .note("Kontynuacja").build());

        res.add(Reservation.builder()
                .id(nextId())
                .client(clients.get(2))
                .employee(e2)
                .service(services.get(2))
                .room(rooms.get(1))
                .startDatetime(LocalDateTime.now().plusDays(3).withHour(9).withMinute(30))
                .endDatetime(LocalDateTime.now().plusDays(3).withHour(10).withMinute(15))
                .status("CONFIRMED")
                .note(null).build());

        res.add(Reservation.builder()
                .id(nextId())
                .client(clients.get(0))
                .employee(e3)
                .service(services.get(2))
                .room(rooms.get(1))
                .startDatetime(LocalDateTime.now().plusDays(4).withHour(16).withMinute(0))
                .endDatetime(LocalDateTime.now().plusDays(4).withHour(16).withMinute(45))
                .status("CANCELLED")
                .note("Klient odwołał").build());

        // attach reservations to employees
        e1.getReservations().add(res.get(0));
        e1.getReservations().add(res.get(1));
        e2.getReservations().add(res.get(2));
        e3.getReservations().add(res.get(3));

        employees.add(e1);
        employees.add(e2);
        employees.add(e3);

        return employees;
    }
}
