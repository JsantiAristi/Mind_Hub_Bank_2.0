package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.Models.CardColor;
import com.mindhub.homebanking.Models.CardType;

import java.time.LocalDate;

public class PaymentDTO {
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate thruDate;
    private String email;
    private double amount;

    public PaymentDTO() {
    }

    public PaymentDTO(CardType type, CardColor color, String number, int cvv, LocalDate thruDate, String email, double amount) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.thruDate = thruDate;
        this.email = email;
        this.amount = amount;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getEmail() {
        return email;
    }

    public double getAmount() {
        return amount;
    }
}
