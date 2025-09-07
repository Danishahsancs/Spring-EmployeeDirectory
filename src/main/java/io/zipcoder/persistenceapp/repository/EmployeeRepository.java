package io.zipcoder.persistenceapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import io.zipcoder.persistenceapp.domain.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByManager_Id(Long managerId);

    List<Employee> findByManagerIsNull();

    List<Employee> findByDepartment_Id(Long departmentId);

    Optional<Employee> findByEmail(String email);

    // fetch hierarchy upwards (manager chain) with JPQL (single hop per query; service will loop)
    @Query("select e.manager from Employee e where e.id = ?1")
    Optional<Employee> findManagerOf(Long employeeId);
}
