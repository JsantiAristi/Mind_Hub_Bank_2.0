package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.AccountType;
import com.mindhub.homebanking.Models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class AccountDTO {
    private long id;
    private AccountType accountType;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean active;
    private Set<TransactionDTO> transactions;

    // Constructores
    public AccountDTO(Account account) {
        this.id = account.getId();
        this.accountType = account.getAccountType();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(toSet());
        this.active = account.isActive();
    }

    // Getter
    public long getId() {
        return id;
    }
    public AccountType getAccountType() {return accountType;}
    public String getNumber() {
        return number;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getBalance() {
        return balance;
    }
    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }
    public boolean isActive() {return active;}
}
