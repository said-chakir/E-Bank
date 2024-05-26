package org.example.e_bank.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Entity
@DiscriminatorValue("SA")

public class SavingAccount extends  BankAccount{
    private  Double interestRate;
}
