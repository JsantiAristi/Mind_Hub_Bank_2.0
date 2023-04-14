package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Client;
import java.util.Set;
import static java.util.stream.Collectors.toSet;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<String> numbers;
    private Set<CardDTO> cards;

    // Constructores
    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.emailAddress = client.getEmailAddress();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(toSet());
        this.loans = client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(toSet());
        this.numbers = this.accounts.stream().map(account -> account.getNumber()).collect(toSet());
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
    public String getEmailAddress() {
        return emailAddress;
    }
    public Set<AccountDTO> getAccounts() {
        return accounts;
    }
    public Set<ClientLoanDTO> getLoans() {return loans;}
    public Set<String> getNumbers() {return numbers;}
    public Set<CardDTO> getCards() {return cards;}
}
