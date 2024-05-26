package org.example.e_bank.dtos;

import lombok.Data;
import org.example.e_bank.enums.AccountStatus;

import java.util.Date;

@Data

public class CurrentBankAccountDTO extends BankAccountDTO {
    private  String id;
    private  Double balance;
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double overDraft;
}
