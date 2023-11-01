package com.amigocode;

import com.amigocode.model.Customer;
import com.amigocode.repository.CustomerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SpringBootApplication
@RequestMapping("api/v1/customers")
public class Main {

    private final CustomerRepository customerRepository;

    public Main(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @GetMapping("/greet")
    public String greet() {
        return "Hello!";
    }

    @GetMapping("/hello")
    public HelloResponse hello() {
        return new HelloResponse(
                "New Hello!",
                List.of("Java", "Golang", "Python"),
                new Person("Nazar", 40, 250000)
        );
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    record NewCustomerRequest(
            String name,
            String email,
            Integer age
    ) {

    }

    @PostMapping
    public void addCustomer(@RequestBody NewCustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setAge(request.age());

        customerRepository.save(customer);
    }

    @DeleteMapping("{customerId}")
    public void deleteCustomer(@PathVariable("customerId") Long id) {
        customerRepository.deleteById(id);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable("customerId") Long id, @RequestBody NewCustomerRequest request) {
        customerRepository.findById(id).ifPresent(
                customer -> {
                    customer.setName(request.name());
                    customer.setEmail(request.email());
                    customer.setAge(request.age());

                    customerRepository.save(customer);
                }
        );
    }

    record Person (String name, int age, int salary) {}

    record HelloResponse(
            String hello,
            List<String> favProgramingLanguages,
            Person person
    ) {}

/*    class HelloResponse {
        private final String hello;

        public HelloResponse(String hello) {
            this.hello = hello;
        }

        public String getHello() {
            return hello;
        }

        @Override
        public String toString() {
            return "HelloResponse{" +
                    "hello='" + hello + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HelloResponse that = (HelloResponse) o;
            return Objects.equals(hello, that.hello);
        }

        @Override
        public int hashCode() {
            return Objects.hash(hello);
        }
    }*/
}
