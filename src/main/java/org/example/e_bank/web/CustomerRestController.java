package org.example.e_bank.web;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.e_bank.dtos.CustomerDTO;
import org.example.e_bank.entities.BankAccount;
import org.example.e_bank.entities.Customer;
import org.example.e_bank.exeption.CustomerNotFoundException;
import org.example.e_bank.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {
    private BankAccountService bankAccountService;
    @GetMapping("/customers")
    private List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/search")
    private List<CustomerDTO> searchCustomers(@RequestParam(name ="keyword" , defaultValue ="") String keyword){
        return bankAccountService.searchCustomers("%"+ keyword + "%");
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable(name="id") Long customerId) throws CustomerNotFoundException {
            return bankAccountService.getCustomer(customerId);
    }
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId, @RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }
    @DeleteMapping("/customers/{id}")
    public  void  deleteCustomer(@PathVariable  Long id){
          bankAccountService.deleteCustomer(id);
    }
}
