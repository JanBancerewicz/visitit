package com.example.visitit.spring;

import com.example.visitit.spring.model.*;
import com.example.visitit.spring.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AppRunner implements CommandLineRunner {

    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final ServiceService serviceService;
    private final RoomService roomService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print("> ");
            String command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "list_employees" -> listAll(employeeService.findAll());
                case "list_clients" -> listAll(clientService.findAll());
                case "list_services" -> listAll(serviceService.findAll());
                case "list_rooms" -> listAll(roomService.findAll());
                case "list_reservations" -> listAll(reservationService.findAll());
                case "list_payments" -> listAll(paymentService.findAll());

                case "add_employee" -> addEmployee();
                case "add_client" -> addClient();
                case "add_service" -> addService();
                case "add_room" -> addRoom();

                case "delete_employee" -> deleteEmployee();
                case "delete_client" -> deleteClient();
                case "delete_service" -> deleteService();
                case "delete_room" -> deleteRoom();

                case "help" -> printMenu();

                case "exit" -> running = false;
                default -> System.out.println("Unknown command");
            }
        }

        System.out.println("Application stopped.");
    }

    private void printMenu() {
        System.out.println("""
                Commands:
                list_employees, list_clients, list_services, list_rooms, list_reservations, list_payments
                add_employee, add_client, add_service, add_room
                delete_employee, delete_client, delete_service, delete_room
                help
                exit
                """);
    }

    private <T> void listAll(List<T> list) {
        if (list.isEmpty()) System.out.println("No records found.");
        else list.forEach(System.out::println);
    }

    private void addEmployee() {
        System.out.print("First name: "); String fn = scanner.nextLine();
        System.out.print("Last name: "); String ln = scanner.nextLine();
        System.out.print("Role: "); String role = scanner.nextLine();

        Employee e = Employee.builder()
                .id(UUID.randomUUID())
                .firstName(fn)
                .lastName(ln)
                .role(role)
                .build();
        employeeService.save(e);
        System.out.println("Employee added: " + e);
    }

    private void addClient() {
        System.out.print("First name: "); String fn = scanner.nextLine();
        System.out.print("Last name: "); String ln = scanner.nextLine();
        System.out.print("Email: "); String email = scanner.nextLine();
        System.out.print("Phone: "); String phone = scanner.nextLine();

        Client c = Client.builder()
                .id(UUID.randomUUID())
                .firstName(fn)
                .lastName(ln)
                .email(email)
                .phone(phone)
                .build();
        clientService.save(c);
        System.out.println("Client added: " + c);
    }

    private void addService() {
        System.out.print("Service name: "); String name = scanner.nextLine();
        System.out.print("Description: "); String desc = scanner.nextLine();
        System.out.print("Duration (min): "); int dur = Integer.parseInt(scanner.nextLine());
        System.out.print("Price: "); BigDecimal price = new BigDecimal(scanner.nextLine());

        Service s = Service.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(desc)
                .durationMin(dur)
                .price(price)
                .build();
        serviceService.save(s);
        System.out.println("Service added: " + s);
    }

    private void addRoom() {
        System.out.print("Room name: "); String name = scanner.nextLine();

        Room r = Room.builder()
                .id(UUID.randomUUID())
                .name(name)
                .build();
        roomService.save(r);
        System.out.println("Room added: " + r);
    }

    private void deleteEmployee() {
        try {
            System.out.print("Enter Employee ID to delete: ");
            UUID id = UUID.fromString(scanner.nextLine());
            Employee e = employeeService.findAll().stream()
                    .filter(emp -> emp.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (e != null) {
                employeeService.delete(e);
                System.out.println("Employee deleted: " + e);
            } else System.out.println("Employee not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private void deleteClient() {
        try{
            System.out.print("Enter Client ID to delete: ");
            UUID id = UUID.fromString(scanner.nextLine());
            Client c = clientService.findAll().stream()
                    .filter(cli -> cli.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (c != null) {
                clientService.delete(c);
                System.out.println("Client deleted: " + c);
            } else System.out.println("Client not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private void deleteService() {
        try{
            System.out.print("Enter Service ID to delete: ");
            UUID id = UUID.fromString(scanner.nextLine());
            Service s = serviceService.findAll().stream()
                    .filter(serv -> serv.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (s != null) {
                serviceService.delete(s);
                System.out.println("Service deleted: " + s);
            } else System.out.println("Service not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }

    private void deleteRoom() {
        try{
            System.out.print("Enter Room ID to delete: ");
            UUID id = UUID.fromString(scanner.nextLine());
            Room r = roomService.findAll().stream()
                    .filter(room -> room.getId().equals(id))
                    .findFirst()
                    .orElse(null);
            if (r != null) {
                roomService.delete(r);
                System.out.println("Room deleted: " + r);
            } else System.out.println("Room not found.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID format.");
        }
    }
}
