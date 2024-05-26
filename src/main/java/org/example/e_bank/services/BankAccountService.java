package org.example.e_bank.services;

import org.example.e_bank.dtos.*;
import org.example.e_bank.entities.BankAccount;
import org.example.e_bank.entities.CurrentAccount;
import org.example.e_bank.entities.Customer;
import org.example.e_bank.entities.SavingAccount;
import org.example.e_bank.exeption.BalanceNotSufficentException;
import org.example.e_bank.exeption.BankAccountNotfoundException;
import org.example.e_bank.exeption.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    List<CustomerDTO> listCustomers();
    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotfoundException;
    void  debit(String accountID , double amount , String description) throws BankAccountNotfoundException, BalanceNotSufficentException;
    void  credit(String accountID , double amount , String description) throws BalanceNotSufficentException, BankAccountNotfoundException;
    void  transfer(String accountIdSource , String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountNotfoundException;

    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotfoundException;

    List<CustomerDTO> searchCustomers(String keyword);

}
