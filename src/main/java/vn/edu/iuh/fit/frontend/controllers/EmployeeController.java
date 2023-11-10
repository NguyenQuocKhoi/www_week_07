package vn.edu.iuh.fit.frontend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.repositories.EmployeeRepository;
import vn.edu.iuh.fit.backend.services.EmployeeService;

@Controller
public class EmployeeController {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private EmployeeRepository employeeRepository;


  @GetMapping("/employees")
  public String showEmployeeListPaging(Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);

    Page<Employee> employeePage = employeeService.findPaginated(currentPage - 1,
        pageSize, "fullname", "asc");

    model.addAttribute("employeePage", employeePage);

    int totalPages = employeePage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "admin/employee/emp-listing";
  }

  @GetMapping("/show-add-formEmployee")
  public String add(Model model) {
    Employee employee = new Employee();
    model.addAttribute("employee", employee);
    return "admin/employee/add-formEmployee";
  }


  @PostMapping("/employees/add")
  public String addEmployee(
      @ModelAttribute("employee") Employee employee,
      BindingResult result, Model model) {
    employeeRepository.save(employee);
    return "redirect:/employees";
  }

  @GetMapping("/employees/delete/{id}")
  public String deleteEmp(@PathVariable("id") long id){
    Employee employee = employeeRepository.findById(id).orElse(new Employee());
    employeeRepository.delete(employee);
    return "redirect:/employees";
  }
}