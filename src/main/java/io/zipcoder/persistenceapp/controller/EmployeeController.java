package io.zipcoder.persistenceapp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.zipcoder.persistenceapp.domain.Employee;
import io.zipcoder.persistenceapp.services.EmployeeService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/API/employees")
public class EmployeeController {

    private final EmployeeService employees;

    public EmployeeController(EmployeeService employees) {
        this.employees = employees;
    }

    // Create employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee e) {
        Employee saved = employees.create(e);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Update employee fields (partial)
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee patch) {
        Employee updated = employees.update(id, patch);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Set manager
    @PutMapping("/{id}/manager/{managerId}")
    public ResponseEntity<Employee> setManager(@PathVariable Long id, @PathVariable Long managerId) {
        Employee updated = employees.setManager(id, managerId);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    // Get attributes of a particular employee (full object)
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getEmployee(id));
    }

    // Get hierarchy upwards (manager, manager's manager, ...)
    @GetMapping("/{id}/manager-chain")
    public ResponseEntity<List<Employee>> getManagerChain(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getManagerChain(id));
    }

    // List employees with no assigned manager
    @GetMapping("/unmanaged")
    public ResponseEntity<List<Employee>> unmanaged() {
        return ResponseEntity.ok(employees.getUnmanaged());
    }

    // GET /api/employees/{id}/hierarchy
    @GetMapping("/{id}/hierarchy")
    public List<Employee> getHierarchy(@PathVariable Long id) {
        return employees.getHierarchy(id);
    }

    // Remove a particular employee or list via query param: /API/employees?ids=1,2,3
    @DeleteMapping
    public ResponseEntity<Void> deleteByIds(@RequestParam("ids") List<Long> ids) {
        employees.deleteEmployeesByIds(ids);
        return ResponseEntity.ok().build();
    }
}
