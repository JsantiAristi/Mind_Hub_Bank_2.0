package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payments;
    private String account;

//    Constructores
    public LoanApplicationDTO() {}

    public LoanApplicationDTO(long id, Double amount, Integer payments, String account) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.account = account;
    }

    //    Getter
    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public String getAccount() {return account;}
}
