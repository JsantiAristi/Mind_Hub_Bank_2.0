package com.mindhub.homebanking.Models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name="native",strategy = "native")
    private long id;
    private AccountType accountType;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean active;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client")
    private Client client;
    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();

    // Constructores
    public Account() {
    }

    public Account(AccountType accountType, String number, LocalDateTime creationDate, double balance, boolean active) {
        this.accountType = accountType;
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.active = active;
    }

    // Método para añadir transacciones
    public void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
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
    public Client getClient() {
        return client;
    }
    public Set<Transaction> getTransactions() { return transactions; }
    public boolean isActive() {return active;}

    // Setter
    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
    public void setNumber(String number) {
        this.number = number;
    }
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public void setActive(boolean active) {this.active = active;}
}
