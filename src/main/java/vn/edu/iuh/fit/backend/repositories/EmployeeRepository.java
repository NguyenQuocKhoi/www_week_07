package vn.edu.iuh.fit.backend.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import vn.edu.iuh.fit.backend.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{


}