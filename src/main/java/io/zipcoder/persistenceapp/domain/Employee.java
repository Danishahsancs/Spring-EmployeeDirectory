// package io.zipcoder.persistenceapp.domain;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import javax.persistence.*;
// import javax.validation.constraints.*;
// import java.time.LocalDate;
// import java.util.HashSet;
// import java.util.Set;

// @Entity
// @Table(name = "employee")
// public class Employee {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @Column(name = "emp_num")
//     private Long id;

//     @NotEmpty(message = "{NotEmpty.employee.firstName}")
//     @Column(name = "first_name", nullable = false)
//     private String firstName;

//     @NotEmpty(message = "{NotEmpty.employee.lastName}")
//     @Column(name = "last_name", nullable = false)
//     private String lastName;

//     @NotEmpty(message = "{NotEmpty.employee.title}")
//     @Column(name = "title", nullable = false)
//     private String title;

//     @NotEmpty(message = "{NotEmpty.employee.phone}")
//     @Column(name = "phone", nullable = false)
//     private String phone;

//     @Email(message = "{Email.employee.email}")
//     @NotEmpty(message = "{NotEmpty.employee.email}")
//     @Column(name = "email", nullable = false, unique = true)
//     private String email;

//     @PastOrPresent(message = "{PastOrPresent.employee.hireDate}")
//     @Column(name = "hire_date", nullable = false)
//     private LocalDate hireDate;

//     // self-referencing manager relationship
//     @ManyToOne
//     @JoinColumn(name = "manager_emp_num")
//     private Employee manager;

//     // inverse side for convenience
//     @OneToMany(mappedBy = "manager")
//     @JsonIgnore
//     private Set<Employee> directReports = new HashSet<>();

//     // department assignment
//     @ManyToOne
//     @JoinColumn(name = "dpt_num")
//     private Department department;

//     // getters/setters
//     public Long getId() { return id; }
//     public void setId(Long id) { this.id = id; }

//     public String getFirstName() { return firstName; }
//     public void setFirstName(String firstName) { this.firstName = firstName; }

//     public String getLastName() { return lastName; }
//     public void setLastName(String lastName) { this.lastName = lastName; }

//     public String getTitle() { return title; }
//     public void setTitle(String title) { this.title = title; }

//     public String getPhone() { return phone; }
//     public void setPhone(String phone) { this.phone = phone; }

//     public String getEmail() { return email; }
//     public void setEmail(String email) { this.email = email; }

//     public LocalDate getHireDate() { return hireDate; }
//     public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

//     public Employee getManager() { return manager; }
//     public void setManager(Employee manager) { this.manager = manager; }

//     public Set<Employee> getDirectReports() { return directReports; }
//     public void setDirectReports(Set<Employee> directReports) { this.directReports = directReports; }

//     public Department getDepartment() { return department; }
//     public void setDepartment(Department department) { this.department = department; }
// }


package io.zipcoder.persistenceapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "emp_num")
    private Long id;

    @NotEmpty(message = "{NotEmpty.employee.firstName}")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty(message = "{NotEmpty.employee.lastName}")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "{NotEmpty.employee.title}")
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty(message = "{NotEmpty.employee.phone}")
    @Column(name = "phone", nullable = false)
    private String phone;

    @Email(message = "{Email.employee.email}")
    @NotEmpty(message = "{NotEmpty.employee.email}")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @PastOrPresent(message = "{PastOrPresent.employee.hireDate}")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    // self-referencing manager relationship
    @ManyToOne
    @JoinColumn(name = "manager_emp_num")
    @JsonIgnoreProperties({"manager", "directReports", "department"}) // Prevent deep nesting
    private Employee manager;

    // inverse side for convenience
    @OneToMany(mappedBy = "manager")
    @JsonIgnore // Always ignore to prevent circular references
    private Set<Employee> directReports = new HashSet<>();

    // department assignment
    @ManyToOne
    @JoinColumn(name = "dpt_num")
    @JsonIgnoreProperties({"employees", "manager"}) // Only show basic department info
    private Department department;

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public Employee getManager() { return manager; }
    public void setManager(Employee manager) { this.manager = manager; }

    public Set<Employee> getDirectReports() { return directReports; }
    public void setDirectReports(Set<Employee> directReports) { this.directReports = directReports; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
}