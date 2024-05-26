package org.example.e_bank.dtos;

import lombok.Data;

@Data
public class TransferRequestDTO {
    private  String accountSource;
    private  double amountDestination;
    private  String description;
    private  String amount;

}
