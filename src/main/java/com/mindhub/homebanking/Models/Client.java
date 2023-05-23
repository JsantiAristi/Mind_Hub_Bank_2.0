package com.mindhub.homebanking.Models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String image;
    private String password;
    private boolean active;
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    // Constructores
    public Client() {
    }

    public Client(String firstName, String lastName, String emailAddress, String image , String password, boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.image = image;
        this.password = password;
        this.active = active;
    }

    // Método para añadir las cuentas
    public void addAccount(Account account){
        account.setClient(this);
        accounts.add(account);
    }

    // Método para añadir los ClientLoan
    public void addClientLoan(ClientLoan clientLoan){
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    // Método para añadir los cards
    public void addCard (Card card){
        card.setClient(this);
        cards.add(card);
    }

    // Método para obtener los Loans
    public List<Loan> getLoans() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    }

    // Getter
    public long getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmailAddress() { return emailAddress; }
    public String getImage() {return image;}
    public Set<Account> getAccounts() {
        return accounts;
    }
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public Set<Card> getCards() {return cards;}
    public String getPassword() {return password;}
    public boolean isActive() {return active;}

    // Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
    public void setImage(String image) {this.image = image;}
    public void setPassword(String password) {this.password = password;}
    public void setActive(boolean active) {this.active = active;}
}
