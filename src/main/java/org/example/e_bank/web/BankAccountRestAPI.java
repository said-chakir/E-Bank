package org.example.e_bank.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.e_bank.dtos.*;
import org.example.e_bank.exeption.BalanceNotSufficentException;
import org.example.e_bank.exeption.BankAccountNotfoundException;
import org.example.e_bank.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBanklAccount(@PathVariable String accountId) throws BankAccountNotfoundException {
        return bankAccountService.getBankAccount(accountId);

    }
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId , @RequestParam(name="page", defaultValue ="0")  int page , @RequestParam(name = "size",defaultValue = "5") int size) throws BankAccountNotfoundException {
          return  bankAccountService.getAccountHistory(accountId,page,size);
    }
    @PostMapping("/accounts/debit")
    public DebitDTO debit( @RequestBody DebitDTO debitDTO) throws BalanceNotSufficentException, BankAccountNotfoundException {
        this.bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(), debitDTO.getDescription());
        return debitDTO;
}
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BalanceNotSufficentException, BankAccountNotfoundException {
        this.bankAccountService.debit(creditDTO.getAccountId(),creditDTO.getAmount(), creditDTO.getDescription());
        return creditDTO;
    }
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BalanceNotSufficentException, BankAccountNotfoundException {
        this.bankAccountService.transfer(
                transferRequestDTO.getAccountSource(),
                transferRequestDTO.getAmount(),
                transferRequestDTO.getAmountDestination());


    }


}

