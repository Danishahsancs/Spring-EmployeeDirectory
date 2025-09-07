// package io.zipcoder.persistenceapp.domain;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import javax.persistence.*;
// import javax.validation.constraints.NotNull;

// import java.util.HashSet;
// import java.util.Set;

// @Entity
// @Table(name = "department")
// public class Department {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "dpt_num")
//     private Long id;

//     @NotNull(message = "{NotEmpty.department.name}")
//     @Column(name = "dpt_name", nullable = false, unique = true)
//     private String name;

//     // Department manager is an employee; nullable until assigned
//     @OneToOne
//     @JoinColumn(name = "manager_emp_num")
//     private Employee manager;

//     // bi-directional convenience (not needed for persistence ops, but handy for reads)
//     @OneToMany(mappedBy = "department")
//     @JsonIgnore // avoid large recursive payloads by default
//     private Set<Employee> employees = new HashSet<>();

//     // getters/setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public String getName() { return name; }
//     public void setName(String name) { this.name = name; }

//     public Employee getManager() { return manager; }
//     public void setManager(Employee manager) { this.manager = manager; }

//     public Set<Employee> getEmployees() { return employees; }
//     public void setEmployees(Set<Employee> employees) { this.employees = employees; }
// }


package io.zipcoder.persistenceapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dpt_num")
    private Long id;

    @NotNull(message = "{NotEmpty.department.name}")
    @Column(name = "dpt_name", nullable = false, unique = true)
    private String name;

    // Department manager is an employee; nullable until assigned
    @OneToOne
    @JoinColumn(name = "manager_emp_num")
    @JsonIgnoreProperties({"manager", "directReports", "department"}) // Only show basic manager info
    private Employee manager;

    // bi-directional convenience (not needed for persistence ops, but handy for reads)
    @OneToMany(mappedBy = "department")
    @JsonIgnore // Always avoid large recursive payloads by default
    private Set<Employee> employees = new HashSet<>();

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }

    public Set<Employee> getEmployees() { return employees; }
    public void setEmployees(Set<Employee> employees) { this.employees = employees; }
}