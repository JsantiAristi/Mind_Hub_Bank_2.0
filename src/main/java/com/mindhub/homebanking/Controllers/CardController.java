package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;

    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCards (Authentication authentication) {
        return cardService.getCardsDTO(authentication);
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> addCard (
            Authentication authentication , @RequestParam String type, @RequestParam String color){

        Client client = clientService.getClientAuthenticated(authentication);

        if ( !type.equalsIgnoreCase("CREDIT")  && !type.equalsIgnoreCase("DEBIT")) {
            return new ResponseEntity<>(type + " is an incorrect type of card", HttpStatus.FORBIDDEN);}
        if ( !color.equalsIgnoreCase("TITANIUM") && !color.equalsIgnoreCase("GOLD") && !color.equalsIgnoreCase("SILVER")) {
            return new ResponseEntity<>(color + " is an incorrect color of card", HttpStatus.FORBIDDEN);}
        if (client == null){
            return new ResponseEntity<>("You can´t create a card, because you are not a client", HttpStatus.FORBIDDEN);};

        for (Card card : client.getCards()) {
            if (card.getType().equals(CardType.valueOf(type.toUpperCase())) && card.getColor().equals(CardColor.valueOf(color.toUpperCase()))) {
                return new ResponseEntity<>("You already have a " + type.toLowerCase() + " " + color.toLowerCase() + " card", HttpStatus.FORBIDDEN);
            }
        }

        Card newCard = new Card(CardType.valueOf(type.toUpperCase()), CardColor.valueOf(color.toUpperCase()), cardService.aleatoryNumberCardsNotRepeat() , cardService.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5));
        clientService.getClientAuthenticated(authentication).addCard(newCard);
        cardService.saveCard(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
