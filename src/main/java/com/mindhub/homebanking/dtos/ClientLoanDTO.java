package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.ClientLoan;

public class ClientLoanDTO {
    private long id;
    private Long loanId;
    private String name;
    private double amount;
    private double finalAmount;
    private int payments;

    // Constructores
    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.finalAmount = clientLoan.getFinalAmount();
        this.payments = clientLoan.getPayments();
    }

    // Getter
    public long getId() {return id;}
    public Long getLoanId() {return loanId;}
    public String getName() {return name;}
    public double getAmount() {return amount;}
    public int getPayments() {return payments;}
    public double getFinalAmount() {return finalAmount;}
}
