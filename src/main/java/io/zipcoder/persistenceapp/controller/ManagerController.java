package io.zipcoder.persistenceapp.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.zipcoder.persistenceapp.domain.Employee;
import io.zipcoder.persistenceapp.services.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/API/managers")
public class ManagerController {

    private final EmployeeService employees;

    public ManagerController(EmployeeService employees) {
        this.employees = employees;
    }

    // Get direct reports
    @GetMapping("/{id}/reports")
    public ResponseEntity<List<Employee>> directReports(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getDirectReports(id));
    }

    // Get direct+indirect reports: /API/managers/{id}/reports/all
    @GetMapping("/{id}/reports/all")
    public ResponseEntity<List<Employee>> allReports(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getAllReportsRecursive(id));
    }

    // Remove all employees under a manager (including indirect)
    @DeleteMapping("/{id}/reports/all")
    public ResponseEntity<Void> deleteAllUnder(@PathVariable Long id) {
        employees.deleteAllUnderManagerRecursive(id);
        return ResponseEntity.ok().build();
    }

    // Remove only direct reports and reassign their reports upwards
    @DeleteMapping("/{id}/reports")
    public ResponseEntity<Void> deleteDirectReports(@PathVariable Long id) {
        employees.deleteDirectReports(id);
        return ResponseEntity.ok().build();
    }
}
