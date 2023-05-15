package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private boolean active;

    // Constructores
    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardholder = card.getClient().getFirstName() + " " + card.getClient().getLastName();
        this.type = card.getType();
        this.color = card.getColor();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.active = card.isActive();
    }

    // Getter
    public long getId() {return id;}
    public String getCardholder() {return cardholder;}
    public CardType getType() {return type;}
    public CardColor getColor() {return color;}
    public String getNumber() {return number;}
    public int getCvv() {return cvv;}
    public LocalDate getFromDate() {return fromDate;}
    public LocalDate getThruDate() {return thruDate;}
    public boolean isActive() {return active;}
}
