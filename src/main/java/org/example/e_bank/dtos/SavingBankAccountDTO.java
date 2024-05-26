package org.example.e_bank.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.e_bank.entities.AccountOperation;
import org.example.e_bank.entities.Customer;
import org.example.e_bank.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Data

public class SavingBankAccountDTO extends BankAccountDTO {
    private  String id;
    private  Double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}
