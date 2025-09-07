package io.zipcoder.persistenceapp.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import io.zipcoder.persistenceapp.domain.Department;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByName(String name);
}
