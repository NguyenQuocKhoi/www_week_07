package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.repositories.EmployeeRepository;

@Service
public class EmployeeService {

  @Autowired
  private  EmployeeRepository employeeRepository;

  public Page<Employee> findPaginated(int pageNo, int pageSize, String sortBy,
      String sortDirection) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
    Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
    return employeeRepository.findAll(pageable);
  }
}
