package vn.edu.iuh.fit.frontend.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner.Mode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.iuh.fit.backend.models.Customer;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.CustomerRepository;
import vn.edu.iuh.fit.backend.services.CustomerService;

@Controller
public class CustomerController {

  @Autowired
  private CustomerService customerService;
  @Autowired
  private CustomerRepository customerRepository;
  @GetMapping("/customers")
  public String showEmployeeListPaging(Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(10);

    Page<Customer> customerPage = customerService.findPaginated(currentPage - 1,
        pageSize, "name", "asc");

    model.addAttribute("customerPage", customerPage);

    int totalPages = customerPage.getTotalPages();
    if (totalPages > 0) {
      List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
          .boxed()
          .collect(Collectors.toList());
      model.addAttribute("pageNumbers", pageNumbers);
    }
    return "admin/customer/cus-listing";
  }

  @GetMapping("/show-add-formCustomer")
  public String add(Model model){
    Customer customer = new Customer();
    model.addAttribute("customer", customer);
    return "admin/customer/add-formCustomer";
  }

  @PostMapping("/customers/add")
  public String addCustomer(@ModelAttribute("customer") Customer customer){
    customerRepository.save(customer);
    return "redirect:/customers";
  }

  @GetMapping("/customers/show-edit-form/{id}")
  public String showUpdateForm(@PathVariable("id") long id, Model model) {
    Customer customer = customerRepository.findById(id).orElse(null);
    model.addAttribute("customer", customer);
    return "admin/customer/editCustomer";
  }

  @PostMapping("admin/customer/editCustomer")
  public String update(
      @ModelAttribute("customer") Customer customer,
      BindingResult result, Model model) {

    customerRepository.save(customer);
    return "redirect:/customers";
  }
}
