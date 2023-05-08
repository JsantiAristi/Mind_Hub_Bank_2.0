package com.mindhub.homebanking.Models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    private double finalAmount;
    private int payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan")
    private Loan loan;

    // Constructores
    public ClientLoan() {
    }

    public ClientLoan( double amount, double finalAmount, int payments) {
        this.amount = amount;
        this.finalAmount = finalAmount;
        this.payments = payments;
    }

    // Getter
    public long getId() {return id;}
    public double getAmount() {return amount;}
    public int getPayments() {return payments;}
    public Client getClient() {return client;}
    public Loan getLoan() {return loan;}
    public double getFinalAmount() {return finalAmount;}

    // Setter
    public void setAmount(double amount) {this.amount = amount;}
    public void setPayments(int payments) {this.payments = payments;}
    public void setClient(Client client) {this.client = client;}
    public void setLoan(Loan loan) {this.loan = loan;}
    public void setFinalAmount(double finalAmount) {this.finalAmount = finalAmount;}
}
