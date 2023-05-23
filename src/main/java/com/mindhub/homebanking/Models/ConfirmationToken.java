package com.mindhub.homebanking.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long tokenid;

    private String confirmationToken;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @OneToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    private Client client;

    public ConfirmationToken() {}

    public ConfirmationToken(Client client) {
        this.client = client;
        createdDate = new Date();
        confirmationToken = UUID.randomUUID().toString();
    }

    public long getTokenid() {return tokenid;}
    public String getConfirmationToken() {return confirmationToken;}
    public Date getCreatedDate() {return createdDate;}
    public Client getClient() {return client;}

    public void setConfirmationToken(String confirmationToken) {this.confirmationToken = confirmationToken;}
    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}
    public void setClient(Client client) {this.client = client;}
}
