package vn.edu.iuh.fit;

import java.time.LocalDate;
import net.datafaker.Faker;
import net.datafaker.providers.base.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Customer;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.CustomerRepository;
import vn.edu.iuh.fit.backend.repositories.EmployeeRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

import java.util.Random;

@SpringBootApplication
public class Week07Application {

  public static void main(String[] args) {
    SpringApplication.run(Week07Application.class, args);
  }

  @Autowired
  private ProductRepository productRepository;
  @Autowired
  private EmployeeRepository employeeRepository;
  @Autowired
      private CustomerRepository customerRepository;
  //      @Bean
  CommandLineRunner createSampleProducts() {
    return args -> {
      Faker faker = new Faker();
//            Random rnd = new Random();
      Device devices = faker.device();
      for (int i = 0; i < 200; i++) {
        Product product = new Product(
            devices.modelName(),
            faker.lorem().paragraph(30),
//                        rnd.nextInt(10)%2==0?"Kg":"piece",
            "piece",
            devices.manufacturer(),
            ProductStatus.ACTIVE
        );
        productRepository.save(product);
      }
    };
  }

//  @Bean
  CommandLineRunner createSampleEmployees(){
    return args -> {
      Faker faker = new Faker();

      for (int i = 0; i<200; i++){
        String address = faker.address().fullAddress();
        LocalDate dob = faker.date().birthday().toLocalDateTime().toLocalDate();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().cellPhone();
        Employee employee = new Employee(address, dob, email, name, phone, EmployeeStatus.ACTIVE);
        employeeRepository.save(employee);
      }
    };
  }
//  @Bean
  CommandLineRunner createSampleCustomers(){
    return args -> {
      Faker faker = new Faker();

      for (int i = 0; i<200; i++){
        String address = faker.address().fullAddress();
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();
        String phone = faker.phoneNumber().cellPhone();
        Customer customer = new Customer(name, email, phone, address);
        customerRepository.save(customer);

      }
    };
  }
}