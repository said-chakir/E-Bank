package org.example.e_bank.exeption;

public class BankAccountNotfoundException extends Exception{
    public BankAccountNotfoundException(String message) {
        super(message);
    }
}
