package com.example.visitit.spring.util;

import com.example.visitit.spring.model.*;
import com.example.visitit.spring.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final ServiceService serviceManager;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;


    private static final int EMPLOYEE_COUNT = 10;
    private static final int CLIENT_COUNT = 10;
    private static final int SERVICE_COUNT = 10;
    private static final int RESERVATION_COUNT = 30;

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
    private static final List<String> PAYMENT_STATUSES = List.of(
            "PAID", "PENDING", "FAILED", "REFUNDED"
    );
    private static final List<String> PAYMENT_METHODS = List.of(
            "CARD", "CASH", "TRANSFER", "BLik"
    );

    private static final Random RANDOM = new Random();

    private static String randomElement(List<String> list) {
        return list.get(RANDOM.nextInt(list.size()));
    }

    private static BigDecimal randomPrice() {
        return SERVICE_PRICES.get(RANDOM.nextInt(SERVICE_PRICES.size()));
    }

    @PostConstruct
    public void init() {
        List<Employee> employees = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();
        List<Reservation> reservations = new ArrayList<>();

        // Klienci
        for (int i = 0; i < CLIENT_COUNT; i++) {
            Client c = Client.builder()
                    .id(UUID.randomUUID())
                    .firstName(randomElement(FIRST_NAMES))
                    .lastName(randomElement(LAST_NAMES))
                    .email("client" + i + "@example.com")
                    .phone("600" + (100000 + RANDOM.nextInt(899999)))
                    .registrationDate(LocalDateTime.now().minusDays(RANDOM.nextInt(60)))
                    .build();
            clientService.save(c);
            clients.add(c);
        }

        // Usługi
        for (int i = 0; i < SERVICE_COUNT; i++) {
            Service s = Service.builder()
                    .id(UUID.randomUUID())
                    .name(randomElement(SERVICE_NAMES))
                    .description("Opis: " + randomElement(DESCRIPTIONS))
                    .durationMin(30 + RANDOM.nextInt(60))
                    .price(randomPrice())
                    .build();
            serviceManager.save(s);
            services.add(s);
        }

        // Pokoje
        for (String name : ROOM_NAMES) {
            Room r = Room.builder().id(UUID.randomUUID()).name(name).build();
            roomService.save(r);
            rooms.add(r);
        }

        // Pracownicy
        for (int i = 0; i < EMPLOYEE_COUNT; i++) {
            Employee e = Employee.builder()
                    .id(UUID.randomUUID())
                    .firstName(randomElement(FIRST_NAMES))
                    .lastName(randomElement(LAST_NAMES))
                    .role(randomElement(ROLES))
                    .description(randomElement(DESCRIPTIONS))
                    .build();
            // losowe przypisanie usług
            Collections.shuffle(services);
            e.getServices().addAll(services.subList(0, 2 + RANDOM.nextInt(3)));
            employeeService.save(e);
            employees.add(e);
        }

        // Rezerwacje i płatności
        for (int i = 0; i < RESERVATION_COUNT; i++) {
            Client c = clients.get(RANDOM.nextInt(clients.size()));
            Employee e = employees.get(RANDOM.nextInt(employees.size()));
            Service s = e.getServices().get(RANDOM.nextInt(e.getServices().size())); // tylko usługi pracownika
            Room r = rooms.get(RANDOM.nextInt(rooms.size()));

            LocalDateTime start = LocalDateTime.now().plusDays(RANDOM.nextInt(30))
                    .withHour(8 + RANDOM.nextInt(10))
                    .withMinute(0).withSecond(0).withNano(0);
            LocalDateTime end = start.plusMinutes(s.getDurationMin());

            Reservation res = Reservation.builder()
                    .id(UUID.randomUUID())
                    .client(c)
                    .employee(e)
                    .service(s)
                    .room(r)
                    .startDatetime(start)
                    .endDatetime(end)
                    .status(randomElement(STATUSES))
                    .note(RANDOM.nextBoolean() ? "Notatka: " + randomElement(DESCRIPTIONS) : null)
                    .build();

            Payment p = Payment.builder()
                    .id(UUID.randomUUID())
                    .reservation(res)
                    .amount(s.getPrice())
                    .status(randomElement(PAYMENT_STATUSES))
                    .method(randomElement(PAYMENT_METHODS))
                    .paymentDate(LocalDateTime.now().minusDays(RANDOM.nextInt(30)))
                    .build();
            res.setPayment(p);

            reservationService.save(res);
            paymentService.save(p);

            e.getReservations().add(res);
            employeeService.save(e);
        }

        System.out.println("Data initialization completed: "
                + EMPLOYEE_COUNT + " employees, "
                + CLIENT_COUNT + " clients, "
                + SERVICE_COUNT + " services, "
                + RESERVATION_COUNT + " reservations.");
    }
}

