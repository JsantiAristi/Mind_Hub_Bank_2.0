package com.mindhub.homebanking.Models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String cardholder;
    private CardType type;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDate fromDate;
    private LocalDate thruDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client")
    private Client client;

    // Constructores
    public Card() {
    }

    public Card(CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate) {
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
    }

    // Método generar número aleatorio para las tarjetas
    public static String aleatoryNumberCards(){
        String numberCard = "";
        for (int i = 0; i < 4; i++) {
            int min = 1000;
            int max = 8999;
            numberCard += (int) (Math.random() * max + min) + "-";
        }
        return numberCard;
    }

    public static int aleatoryNumberCvv(){
            int min = 100;
            int max = 899;
        return (int) (Math.random() * max + min);
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
    public Client getClient() {return client;}

    // Setter
    public void setCardholder(String cardholder) {this.cardholder = cardholder;}
    public void setType(CardType type) {this.type = type;}
    public void setColor(CardColor color) {this.color = color;}
    public void setNumber(String number) {this.number = number;}
    public void setCvv(int cvv) {this.cvv = cvv;}
    public void setFromDate(LocalDate fromDate) {this.fromDate = fromDate;}
    public void setThruDate(LocalDate thruDate) {this.thruDate = thruDate;}
    public void setClient(Client client) {this.client = client;}
}
