package com.example.visitit.se;

import com.example.visitit.se.model.*;
import com.example.visitit.se.service.ReservationService;
import com.example.visitit.se.util.DataGenerator;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) throws Exception {
        // 1. Data generating (categories = employees, elements = reservations)
        List<Employee> employees = DataGenerator.sampleEmployeesWithReservations(5,5,3, 8);

        System.out.println("=== 2) Employees and their reservations (original order) ===");
        // 2. Nested forEach lambda printing categories and elements in original order
        employees.forEach(emp -> {
            System.out.println(emp);
            emp.getReservations().forEach(res -> System.out.println("  -> " + res));
        });

        // 3. Using single Stream API pipeline create Set collection of all elements (from all categories)
        Set<Reservation> allReservationsSet = employees.stream()
                .flatMap(e -> e.getReservations().stream())
                .collect(Collectors.toCollection(LinkedHashSet::new)); // keep encounter order
        System.out.println("\n=== 3) All reservations as Set ===");
        allReservationsSet.forEach(System.out::println);

        // 4. Filter elements (by status = CONFIRMED) and then sort (by startDatetime)
        System.out.println("\n=== 4) Filtered (status=CONFIRMED) and sorted by startDatetime ===");
        allReservationsSet.stream()
                .filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus()))
                .sorted(Comparator.comparing(Reservation::getStartDatetime, Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(System.out::println);

        // 5. Transform to DTOs, sort by natural order (startDatetime) and collect into List, then print
        System.out.println("\n=== 5) DTOs sorted by startDatetime ===");
        List<ReservationDTO> dtoList = employees.stream()
                .flatMap(e -> e.getReservations().stream())
                .map(Main::toDto)
                .sorted()
                .collect(Collectors.toList());
        dtoList.forEach(System.out::println);

        // 6. Serialization: store collection of categories (employees) to binary file and read it back
        System.out.println("\n=== 6) Serialization of employees (with reservations) ===");
        File serFile = new File("ser/employees.ser");
        serFile.getParentFile().mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
            oos.writeObject(employees);
        }
        System.out.println("Saved employees to " + serFile.getAbsolutePath());

        // read back
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
            Object o = ois.readObject();
            if (o instanceof List) {
                List<?> read = (List<?>) o;
                System.out.println("Read back objects:");
                read.forEach(emp -> {
                    System.out.println(emp);
                    if (emp instanceof Employee e) {
                        e.getReservations().forEach(r -> System.out.println("   -> " + r));
                    }
                });
            }
        }

        // 7. Parallel pipelines with custom thread pool (ForkJoinPool)
        System.out.println("\n=== 7) Parallel processing per employee with custom ForkJoinPool ===");
        int[] poolSizes = {1, 2, 4};
        for (int poolSize : poolSizes) {
            ForkJoinPool pool = new ForkJoinPool(poolSize);
            System.out.printf("Running with pool size = %d%n", poolSize);
            try {
                pool.submit(() ->
                        employees.parallelStream().forEach(emp -> {
                            // simulate work per category
                            System.out.println(Thread.currentThread().getName() + " processing " + emp);
                            emp.getReservations().forEach(r -> {
                                try {
                                    Thread.sleep(100); // simulate work
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                                System.out.println("   " + Thread.currentThread().getName() + " -> " + r);
                            });
                        })
                ).get(); // wait for completion
            } finally {
                pool.shutdown();
                pool.awaitTermination(5, TimeUnit.SECONDS);
            }
            System.out.println("Pool size " + poolSize + " finished.\n");
        }

        // Example usage of ReservationService (CRUD + serialization)
        ReservationService service = new ReservationService();
        allReservationsSet.forEach(service::save);
        System.out.println("Service stored reservations: " + service.findAll().size());

        // store reservations using ReservationService helper
        File resFile = new File("ser/reservations.ser");
        resFile.getParentFile().mkdirs();
        service.saveToFile(resFile);
        System.out.println("Reservations saved to " + resFile.getAbsolutePath());

        // load back
        ReservationService loadedService = new ReservationService();
        loadedService.loadFromFile(resFile);
        System.out.println("Loaded reservations: " + loadedService.findAll().size());

        System.out.println("\n=== MAIN FINISHED ===");
    }

    private static ReservationDTO toDto(Reservation r) {
        return ReservationDTO.builder()
                .id(r.getId())
                .clientFullName(r.getClient() != null ? (r.getClient().getFirstName() + " " + r.getClient().getLastName()) : null)
                .employeeFullName(r.getEmployee() != null ? (r.getEmployee().getFirstName() + " " + r.getEmployee().getLastName()) : null)
                .serviceName(r.getService() != null ? r.getService().getName() : null)
                .roomName(r.getRoom() != null ? r.getRoom().getName() : null)
                .startDatetime(r.getStartDatetime())
                .endDatetime(r.getEndDatetime())
                .status(r.getStatus())
                .build();
    }
}
