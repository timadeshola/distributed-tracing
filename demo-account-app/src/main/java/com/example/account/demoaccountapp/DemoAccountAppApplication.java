package com.example.account.demoaccountapp;

import brave.sampler.Sampler;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class DemoAccountAppApplication {


    @Bean
    public Sampler sampler() {
        return Sampler.ALWAYS_SAMPLE;
    }

    @FeignClient(name = "customer-service")
    @RibbonClient("account-service")
    static interface FeignService {

        @PostMapping("customer")
        public ResponseEntity<CustomerResponse> createAccount(@RequestParam String name, @RequestParam String tel);

        @GetMapping("customer")
        public ResponseEntity<CustomerResponse> getCustomerById(@RequestParam Long id);
    }

    static interface AccountService {

        Account createAccount(String name, String tel);

        Account getAccountById(Long id);

        List<Account> findAllAccounts();
    }

    @Service
    @RequiredArgsConstructor
    static class AccountServiceImpl implements AccountService {

        private final FeignService feignService;

        List<Account> accounts = new ArrayList<>();

        @Override
        public Account createAccount(String name, String tel) {
            Account account = new Account();
            ResponseEntity<CustomerResponse> response = feignService.createAccount(name, tel);
            if(response.getBody() != null && response.getStatusCode().is2xxSuccessful()) {
                account.setId(response.getBody().getId());
                account.setAccountName(response.getBody().getName());
                account.setAccountNumber(response.getBody().getAccountNo());
                accounts.add(account);
                return account;
            }
            return null;
        }

        @Override
        public Account getAccountById(Long id) {
            for (Account account : accounts) {
                if(account.getId().equals(id)) {
                    return account;
                }
            }
            return null;
        }

        @Override
        public List<Account> findAllAccounts() {
            return accounts;
        }
    }

    @RestController
    @RequiredArgsConstructor
    static class AccountController {

        private final AccountService accountService;

        @PostMapping("account")
        public ResponseEntity<Account> createCustomer(@RequestParam String name, @RequestParam String tel) {
            Account customer = accountService.createAccount(name, tel);
            return new ResponseEntity<>(customer, HttpStatus.CREATED);
        }

        @GetMapping("account")
        public ResponseEntity<Account> getCustomerById(@RequestParam Long id) {
            Account customer = accountService.getAccountById(id);
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }

        @GetMapping("accounts")
        public ResponseEntity<List<Account>> findAllAccounts() {
            List<Account> customers = accountService.findAllAccounts();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoAccountAppApplication.class, args);
    }

}

@Data
class Account {
    private Long id;
    private String accountName;
    private String accountNumber;
}

@Data
class CustomerResponse {
    private Long id;
    private String name;
    private String telephone;
    private String accountNo;
}
