package org.example.e_bank.dtos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.e_bank.entities.BankAccount;
import org.example.e_bank.enums.OperationType;

import java.util.Date;

@Data
public class AccountOperationDTO {

    private Long id;
    private Date operationDate;
    private  Double amount;
    private OperationType type;
    private String description;
}
