package org.example.e_bank.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.e_bank.dtos.*;
import org.example.e_bank.entities.*;
import org.example.e_bank.enums.OperationType;
import org.example.e_bank.exeption.BalanceNotSufficentException;
import org.example.e_bank.exeption.BankAccountNotfoundException;
import org.example.e_bank.exeption.CustomerNotFoundException;
import org.example.e_bank.mappers.BankAccountMapperImp;
import org.example.e_bank.repository.AccountOperationRepository;
import org.example.e_bank.repository.BankAccountRepository;
import org.example.e_bank.repository.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service @Transactional @AllArgsConstructor @Slf4j

public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private  BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImp dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new costomer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw  new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount=new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setCustomer(customer);
        currentAccount.setOverDraft(overDraft);
       CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer=customerRepository.findById(customerId).orElse(null);
        if (customer==null)
            throw  new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount=new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setCustomer(customer);
        savingAccount.setInterestRate(interestRate);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);
    }
    @Override
    public List<CustomerDTO> listCustomers() {

        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotfoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotfoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount) {
            SavingAccount savingAccount = (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        }else {
            CurrentAccount currentAccount = (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
              }
    }


    @Override
    public void debit(String accountID, double amount, String description) throws BankAccountNotfoundException, BalanceNotSufficentException {
        BankAccount bankAccount=bankAccountRepository.findById(accountID)
                .orElseThrow(()->new BankAccountNotfoundException("BankAccount not found"));
        if(bankAccount.getBalance()<amount)
            throw  new BalanceNotSufficentException("balance not sufficient");
        AccountOperation accountOperation =new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);

    }

    @Override
    public void credit(String accountID, double amount, String description) throws  BankAccountNotfoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountID)
                .orElseThrow(()->new BankAccountNotfoundException("BankAccount not found"));
        AccountOperation accountOperation =new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BalanceNotSufficentException, BankAccountNotfoundException {
          debit(accountIdSource,amount,"transfer to"+accountIdDestination);
          credit(accountIdDestination,amount,"transfer"+accountIdSource);


    }
    @Override
   public List<BankAccountDTO> bankAccountList(){
        List<BankAccount>  bankAccounts = bankAccountRepository.findAll();

        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount){
                SavingAccount savingAccount=(SavingAccount)  bankAccount;
                return  dtoMapper.fromSavingBankAccount(savingAccount);

            }
            else {
                CurrentAccount currentAccount=(CurrentAccount)  bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);

            }
        }).collect(Collectors.toList());
        return bankAccountDTOS;
   }


   @Override
   public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
       Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()-> new  CustomerNotFoundException("customer not found"));
       return dtoMapper.fromCustomer(customer);
   }
   @Override
   public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new costomer");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }
    @Override
    public void deleteCustomer(Long customerId){
      customerRepository.deleteById(customerId);
    }
    @Override
    public  List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        return accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotfoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if (bankAccount==null) throw  new BankAccountNotfoundException("Account not found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId, PageRequest.of(page,size));
        AccountHistoryDTO accountHistoryDTO =new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return  accountHistoryDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        List<Customer> customers = customerRepository.searchCustomers(keyword);
        List<CustomerDTO> customerDTOS =customers.stream().map(cust->dtoMapper.fromCustomer(cust)).collect(Collectors.toList());
        return customerDTOS;
    }


}
