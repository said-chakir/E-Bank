package org.example.e_bank.mappers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.e_bank.dtos.AccountOperationDTO;
import org.example.e_bank.dtos.CurrentBankAccountDTO;
import org.example.e_bank.dtos.CustomerDTO;
import org.example.e_bank.dtos.SavingBankAccountDTO;
import org.example.e_bank.entities.AccountOperation;
import org.example.e_bank.entities.CurrentAccount;
import org.example.e_bank.entities.Customer;
import org.example.e_bank.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImp {
    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer,customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);

        return customer;
    }
    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
         SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
         BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
         savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
         savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());
         return  savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO savingBankAccountDTO){
      SavingAccount savingAccount=new SavingAccount();
      BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
      savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
      return savingAccount;

    }
    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
       CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
       BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
       currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
       currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());
       return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;

    }


    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO=new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);
        return  accountOperationDTO;
    }


}
