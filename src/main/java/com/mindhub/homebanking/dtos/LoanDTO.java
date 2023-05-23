package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Loan;
import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private String descriptionLoan;
    private List<Integer> payments;
    private double interest;

    //    Constructores
    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
        this.descriptionLoan = loan.getDescriptionLoan();
        this.interest = loan.getInterest();
    }

    //    Getter
    public long getId() {return id;}
    public String getName() {return name;}
    public double getMaxAmount() {return maxAmount;}
    public List<Integer> getPayments() {return payments;}
    public String getDescriptionLoan() {return descriptionLoan;}
    public double getInterest() {return interest;}
}
