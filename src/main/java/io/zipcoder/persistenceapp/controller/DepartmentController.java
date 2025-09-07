package io.zipcoder.persistenceapp.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.zipcoder.persistenceapp.domain.Department;
import io.zipcoder.persistenceapp.domain.Employee;
import io.zipcoder.persistenceapp.services.DepartmentService;
import io.zipcoder.persistenceapp.services.EmployeeService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/API/departments")
public class DepartmentController {

    private final DepartmentService departments;
    private final EmployeeService employees;

    public DepartmentController(DepartmentService departments, EmployeeService employees) {
        this.departments = departments;
        this.employees = employees;
    }

    // Create a Department
    @PostMapping
    public ResponseEntity<Department> create(@Valid @RequestBody Department d) {
        return new ResponseEntity<>(departments.create(d), HttpStatus.CREATED);
    }

    // Set a new department manager
    @PutMapping("/{id}/manager/{managerEmpId}")
    public ResponseEntity<Department> setManager(@PathVariable Long id, @PathVariable Long managerEmpId) {
        return ResponseEntity.ok(departments.setManager(id, managerEmpId));
    }

    // Change department name
    @PutMapping("/{id}/name")
    public ResponseEntity<Department> rename(@PathVariable Long id, @RequestParam("value") String newName) {
        return ResponseEntity.ok(departments.rename(id, newName));
    }

    // Get all employees of a department
    @GetMapping("/{id}/employees")
    public ResponseEntity<List<Employee>> listEmployees(@PathVariable Long id) {
        return ResponseEntity.ok(employees.getByDepartment(id));
    }

    // Remove all employees from a department
    @DeleteMapping("/{id}/employees")
    public ResponseEntity<Void> deleteAllInDepartment(@PathVariable Long id) {
        employees.deleteByDepartment(id);
        return ResponseEntity.ok().build();
    }

    // Merge departments by names: /API/departments/merge?from=B&to=A
    @PostMapping("/merge")
    public ResponseEntity<Void> mergeByNames(@RequestParam("from") String fromName,
                                             @RequestParam("to") String toName) {
        departments.mergeByNames(fromName, toName);
        return ResponseEntity.ok().build();
    }
}
