package com.example.visitit.se;

import com.example.visitit.se.util.DataGenerator;
import com.example.visitit.se.model.Employee;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    public void dataGeneratorProducesEmployees() {
        List<Employee> employees = DataGenerator.sampleEmployeesWithReservations(5,5,3,8);
        assertNotNull(employees);
        assertTrue(employees.size() >= 1);
        employees.forEach(e -> assertNotNull(e.getReservations()));
    }
}
