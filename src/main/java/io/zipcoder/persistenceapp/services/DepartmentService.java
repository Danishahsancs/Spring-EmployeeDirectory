package io.zipcoder.persistenceapp.services;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.zipcoder.persistenceapp.domain.Department;
import io.zipcoder.persistenceapp.domain.Employee;
import io.zipcoder.persistenceapp.exception.ResourceNotFoundException;
import io.zipcoder.persistenceapp.repository.DepartmentRepository;
import io.zipcoder.persistenceapp.repository.EmployeeRepository;

import java.util.List;

@Service
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepo;
    private final EmployeeRepository employeeRepo;

    public DepartmentService(DepartmentRepository departmentRepo, EmployeeRepository employeeRepo) {
        this.departmentRepo = departmentRepo;
        this.employeeRepo = employeeRepo;
    }

    public Department create(Department d) {
        return departmentRepo.save(d);
    }

    public Department setManager(Long dptId, Long managerEmpId) {
        Department d = getById(dptId);
        Employee mgr = employeeRepo.findById(managerEmpId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee " + managerEmpId + " not found"));
        d.setManager(mgr);
        // ensure manager is in department
        mgr.setDepartment(d);
        mgr.setManager(mgr.getManager()); // no change; just explicit write-through
        employeeRepo.save(mgr);
        return departmentRepo.save(d);
    }

    public Department rename(Long dptId, String newName) {
        Department d = getById(dptId);
        d.setName(newName);
        return departmentRepo.save(d);
    }

    public Department getById(Long id) {
        return departmentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department " + id + " not found"));
    }

    public void mergeByNames(String fromName, String toName) {
        Department from = departmentRepo.findByName(fromName)
                .orElseThrow(() -> new ResourceNotFoundException("Department '" + fromName + "' not found"));
        Department to = departmentRepo.findByName(toName)
                .orElseThrow(() -> new ResourceNotFoundException("Department '" + toName + "' not found"));

        // move manager of B to report to manager of A
        Employee mgrFrom = from.getManager();
        Employee mgrTo = to.getManager(); // may be null in edge cases
        if (mgrFrom != null) {
            mgrFrom.setManager(mgrTo);
            mgrFrom.setDepartment(to);
            employeeRepo.save(mgrFrom);
        }

        // move all other employees to department A
        List<Employee> movers = employeeRepo.findByDepartment_Id(from.getId());
        for (Employee e : movers) {
            e.setDepartment(to);
            employeeRepo.save(e);
        }

        // optional: delete old department or keep it; here we keep but empty
    }
}
