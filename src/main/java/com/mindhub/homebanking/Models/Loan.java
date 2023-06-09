package com.mindhub.homebanking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String name;
    private double maxAmount;
    private String descriptionLoan;
    private double interest;
    @ElementCollection
    @Column(name = "payment")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy="loan", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    // Constructores
    public Loan() {
    }

    public Loan(String name, double maxAmount, List<Integer> payments, String descriptionLoan, double interest) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.descriptionLoan = descriptionLoan;
        this.interest= interest;
    }

    // Método para añadir los ClientLoan
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setLoan(this);
        clientLoans.add(clientLoan);
    }

    // Método para obtener los Clientes
    public List<Client> getClients() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(toList());
    }

    // Getter
    public long getId() { return id; }
    public String getName() {
        return name;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public String getDescriptionLoan() {return descriptionLoan;}
    public double getInterest() {return interest;}

    // Setter
    public void setName(String name) {
        this.name = name;
    }
    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }
    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }
    public void setDescriptionLoan(String descriptionLoan) {this.descriptionLoan = descriptionLoan;}
    public void setInterest(double interest) {this.interest = interest;}
}
