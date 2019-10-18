package com.example.customer.democustomerapp;

import brave.sampler.Sampler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class DemoCustomerAppApplication {

    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }



    public static void main(String[] args) {
        SpringApplication.run(DemoCustomerAppApplication.class, args);
    }

    static interface CustomerService {
        Customer createCustomer(String name, String telephone);

        Customer getCustomerById(Long id);

        List<Customer> findAllCustomers();
    }

    @Service
    static class CustomerServiceImpl implements CustomerService {

        List<Customer> customers = new ArrayList<>();

        @Override
        public Customer createCustomer(String name, String telephone) {
            Customer customer = new Customer();
            for (Customer cust : customers) {
                if(cust.getName().equalsIgnoreCase(name)) {
                    throw new IllegalArgumentException("Name already exist");
                }
            }
            customer.setId(Long.valueOf(RandomStringUtils.randomNumeric(4)));
            customer.setName(name);
            customer.setTelephone(telephone);
            customer.setAccountNo(RandomStringUtils.randomNumeric(10));
            customers.add(customer);
            return customer;
        }

        @Override
        public Customer getCustomerById(Long id) {
            for (Customer customer : customers) {
                if(customer.getId().equals(id)) {
                    return customer;
                }
            }
            return null;
        }

        @Override
        public List<Customer> findAllCustomers() {
            return customers;
        }
    }

    @RestController
    @RequiredArgsConstructor
    static class CustomerController {
        private final CustomerService customerService;

        @PostMapping("customer")
        public ResponseEntity<Customer> createCustomer(@RequestParam String name, @RequestParam String tel) {
            Customer customer = customerService.createCustomer(name, tel);
            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        }

        @GetMapping("customer")
        public ResponseEntity<Customer> getCustomerById(@RequestParam Long id) {
            Customer customer = customerService.getCustomerById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }

        @GetMapping("customers")
        public ResponseEntity<List<Customer>> findAllCustomers() {
            List<Customer> customers = customerService.findAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }
    }

}

@Data
class Customer {
    private Long id;
    private String name;
    private String telephone;
    private String accountNo;
}
