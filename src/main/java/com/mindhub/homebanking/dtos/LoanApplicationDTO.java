package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private Integer payments;
    private String account;

//    Constructores
    public LoanApplicationDTO() {}

    //    Getter
    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public Integer getPayments() {return payments;}
    public String getAccount() {return account;}
}
