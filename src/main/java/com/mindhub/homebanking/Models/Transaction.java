package com.mindhub.homebanking.Models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;
    private boolean active;
    private double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account")
    private Account account;

    // Constructores
    public Transaction() {
    }

    public Transaction(TransactionType type, double amount, String description, LocalDateTime date, boolean active, double balance) {
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.active = active;
        this.balance = balance;
    }

    //Método para hacer un String
    public static String stringToAccount(String initialAccount){
        return "Transaction of " + initialAccount;
    }
    // Getter
    public long getId() {
        return id;
    }
    public TransactionType getType() {
        return type;
    }
    public double getAmount() {
        return amount;
    }
    public String getDescription() {
        return description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public Account getAccount() {
        return account;
    }
    public boolean isActive() {return active;}
    public double getBalance() {return balance;}

    // Setter
    public void setType(TransactionType type) {
        this.type = type;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public void setActive(boolean active) {this.active = active;}
    public void setBalance(double balance) {this.balance = balance;}
}
