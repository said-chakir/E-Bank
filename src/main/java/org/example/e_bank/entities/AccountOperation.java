package org.example.e_bank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.e_bank.enums.OperationType;

import java.util.Date;

@Data@NoArgsConstructor@AllArgsConstructor@Entity
public class AccountOperation {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)

    private Long id;
    private Date operationDate;
    private  Double amount;
    @Enumerated(EnumType.STRING)
    private OperationType type;
    @ManyToOne
    private  BankAccount bankAccount;
    private String description;
}
