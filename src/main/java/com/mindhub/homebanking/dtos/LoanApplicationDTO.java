package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private int payments;
    private String account;
    private String name;

//    Constructores
    public LoanApplicationDTO() {}

    public LoanApplicationDTO(long id, Double amount, int payments, String account) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.account = account;
    }

    //    Getter
    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public int getPayments() {return payments;}
    public String getAccount() {return account;}
    public String getName() {return name;}
}
