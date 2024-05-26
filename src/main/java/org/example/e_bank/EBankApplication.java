package org.example.e_bank;

import lombok.Data;
import org.example.e_bank.dtos.BankAccountDTO;
import org.example.e_bank.dtos.CurrentBankAccountDTO;
import org.example.e_bank.dtos.CustomerDTO;
import org.example.e_bank.dtos.SavingBankAccountDTO;
import org.example.e_bank.entities.*;
import org.example.e_bank.enums.AccountStatus;
import org.example.e_bank.enums.OperationType;
import org.example.e_bank.exeption.BalanceNotSufficentException;
import org.example.e_bank.exeption.BankAccountNotfoundException;
import org.example.e_bank.exeption.CustomerNotFoundException;
import org.example.e_bank.repository.AccountOperationRepository;
import org.example.e_bank.repository.BankAccountRepository;
import org.example.e_bank.repository.CustomerRepository;
import org.example.e_bank.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EBankApplication {

    public static void main(String[] args) {

        SpringApplication.run(EBankApplication.class, args);
    }
     @Bean
             CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return  args -> {
            Stream.of("said","chakir","omar").forEach(name->{
                CustomerDTO customer=new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);

            });
            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random()*90000,90000,customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random()*120000,5.5, customer.getId());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
            List<BankAccountDTO> bankAccounts =bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount:bankAccounts){
                for (int i=0 ; i<10 ; i++){
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO)bankAccount).getId();
                    }
                    else {
                        accountId=((CurrentBankAccountDTO)bankAccount).getId();
                    }
                    bankAccountService.credit(accountId, 10000+Math.random()*12000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()+9000,"debit");

                }

            }

        };
     }




    //@Bean
    CommandLineRunner start (CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository){
        return  args -> {
            Stream.of("hassan","yassine","aicha").forEach(name->{
                Customer customer=new Customer();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                customerRepository.save(customer);
            });
           customerRepository.findAll().forEach(cust->{
               CurrentAccount currentAccount=new CurrentAccount();
               currentAccount.setId(UUID.randomUUID().toString());
               currentAccount.setBalance(Math.random()*90000);
               currentAccount.setCreatedAt(new Date());
               currentAccount.setStatus(AccountStatus.CREATED);
               currentAccount.setCustomer(cust);
               currentAccount.setOverDraft(9000.0);
               bankAccountRepository.save(currentAccount);

               SavingAccount savingAccount=new SavingAccount();
               savingAccount.setBalance(Math.random()*90000);
               savingAccount.setCreatedAt(new Date());
               savingAccount.setId(UUID.randomUUID().toString());
               savingAccount.setStatus(AccountStatus.CREATED);
               savingAccount.setCustomer(cust);
               savingAccount.setInterestRate(5.5);
               bankAccountRepository.save(savingAccount);

           });
           bankAccountRepository.findAll().forEach(acc->{
               for (int i=0 ; i<5; i++){
                   AccountOperation accountOperation=new AccountOperation();
                   accountOperation.setOperationDate(new Date());
                   accountOperation.setAmount(Math.random()*12000);
                   accountOperation.setType(Math.random()>0.5? OperationType.DEBIT:OperationType.CREDIT);
                   accountOperationRepository.save(accountOperation);
               }

           });

        };
    }


}
