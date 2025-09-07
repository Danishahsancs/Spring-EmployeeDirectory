package io.zipcoder.persistenceapp.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zipcoder.persistenceapp.domain.Department;
import io.zipcoder.persistenceapp.domain.Employee;
import io.zipcoder.persistenceapp.exception.ResourceNotFoundException;
import io.zipcoder.persistenceapp.repository.DepartmentRepository;
import io.zipcoder.persistenceapp.repository.EmployeeRepository;

import java.util.*;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;

    public EmployeeService(EmployeeRepository employeeRepo, DepartmentRepository departmentRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    public Employee create(Employee e) {
        // if manager present, inherit manager's department
        if (e.getManager() != null) {
            Employee mgr = getEmployee(e.getManager().getId());
            e.setManager(mgr);
            e.setDepartment(mgr.getDepartment());
        }
        // if department provided without manager, keep as is
        return employeeRepo.save(e);
    }

    public Employee getEmployee(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee " + id + " not found"));
    }

    public Employee update(Long id, Employee patch) {
        Employee existing = getEmployee(id);
        if (patch.getFirstName() != null) existing.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null) existing.setLastName(patch.getLastName());
        if (patch.getTitle() != null) existing.setTitle(patch.getTitle());
        if (patch.getPhone() != null) existing.setPhone(patch.getPhone());
        if (patch.getEmail() != null) existing.setEmail(patch.getEmail());
        if (patch.getHireDate() != null) existing.setHireDate(patch.getHireDate());
        if (patch.getDepartment() != null) {
            Department d = departmentRepo.findById(patch.getDepartment().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department " + patch.getDepartment().getId() + " not found"));
            existing.setDepartment(d);
        }
        return employeeRepo.save(existing);
    }

    public Employee setManager(Long empId, Long managerId) {
        Employee emp = getEmployee(empId);
        Employee mgr = getEmployee(managerId);
        emp.setManager(mgr);
        // rule: assigning a manager also moves the employee into manager’s department
        emp.setDepartment(mgr.getDepartment());
        return employeeRepo.save(emp);
    }

    public List<Employee> getDirectReports(Long managerId) {
        getEmployee(managerId); // verify exists
        return employeeRepo.findByManager_Id(managerId);
    }

    public List<Employee> getAllReportsRecursive(Long managerId) {
        getEmployee(managerId); // verify exists
        List<Employee> out = new ArrayList<>();
        Deque<Long> q = new ArrayDeque<>();
        q.add(managerId);
        while (!q.isEmpty()) {
            Long current = q.removeFirst();
            List<Employee> direct = employeeRepo.findByManager_Id(current);
            for (Employee e : direct) {
                out.add(e);
                q.add(e.getId());
            }
        }
        return out;
    }

    // Replace your getManagerChain method with this fixed version:

public List<Employee> getManagerChain(Long empId) {
    Employee e = getEmployee(empId);
    List<Employee> chain = new ArrayList<>();
    Set<Long> visited = new HashSet<>(); // Prevent infinite loops
    
    Long currentId = empId;
    while (currentId != null && !visited.contains(currentId)) {
        visited.add(currentId);
        
        // Use the repository query to get the manager
        Optional<Employee> managerOpt = employeeRepo.findManagerOf(currentId);
        if (managerOpt.isPresent()) {
            Employee manager = managerOpt.get();
            chain.add(manager);
            currentId = manager.getId();
        } else {
            break; // No more managers up the chain
        }
    }
    return chain;
}

    public List<Employee> getUnmanaged() {
        return employeeRepo.findByManagerIsNull();
    }

    public List<Employee> getByDepartment(Long dptId) {
        return employeeRepo.findByDepartment_Id(dptId);
    }

    public void deleteEmployeesByIds(List<Long> ids) {
        ids.forEach(this::deleteEmployeeReassignReportsUpwards);
    }

    public void deleteByDepartment(Long dptId) {
        departmentRepo.findById(dptId)
                .orElseThrow(() -> new ResourceNotFoundException("Department " + dptId + " not found"));
        List<Employee> inDept = employeeRepo.findByDepartment_Id(dptId);
        inDept.forEach(this::deleteEmployeeReassignReportsUpwards);
    }

    public void deleteAllUnderManagerRecursive(Long managerId) {
        // deletes every descendant (but not the manager)
        List<Employee> all = getAllReportsRecursive(managerId);
        all.forEach(this::deleteEmployeeReassignReportsUpwards);
    }

    public void deleteDirectReports(Long managerId) {
        List<Employee> direct = getDirectReports(managerId);
        for (Employee child : direct) {
            deleteEmployeeReassignReportsUpwards(child);
        }
    }

    private void deleteEmployeeReassignReportsUpwards(Employee toDelete) {
        // reassign their direct reports to the next manager up
        Employee nextManager = toDelete.getManager();
        List<Employee> direct = employeeRepo.findByManager_Id(toDelete.getId());
        for (Employee child : direct) {
            child.setManager(nextManager);
            // inherit department from new manager (if any)
            child.setDepartment(nextManager != null ? nextManager.getDepartment() : child.getDepartment());
            employeeRepo.save(child);
        }
        employeeRepo.deleteById(toDelete.getId());
    }

    private void deleteEmployeeReassignReportsUpwards(Long empId) {
        Employee e = getEmployee(empId);
        deleteEmployeeReassignReportsUpwards(e);
    }

     public List<Employee> getHierarchy(Long empId) {
        List<Employee> chain = new ArrayList<>();
        Employee current = employeeRepo.findById(empId)
                                             .orElseThrow(() -> new RuntimeException("Employee not found"));
        while (current.getManager() != null) {
            current = current.getManager();
            chain.add(current);
        }
        return chain; // ordered bottom → top
    }
}
