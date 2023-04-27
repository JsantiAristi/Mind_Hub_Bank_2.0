package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRespository;
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
import static java.util.stream.Collectors.toList;

@RestController
public class CardController {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRespository clientRespository;

    @RequestMapping("/api/clients/current/cards")
    public List<CardDTO> getCards (Authentication authentication) {
        return new ClientDTO(clientRespository.findByEmailAddress(authentication.getName())).getCards().stream().collect(toList());
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> addCard (
            Authentication authentication , @RequestParam String type, @RequestParam String color){
        if ( type.isEmpty() ) {
            return new ResponseEntity<>("Type is required", HttpStatus.FORBIDDEN);
        }
        if ( color.isEmpty() ) {
            return new ResponseEntity<>("Color is required", HttpStatus.FORBIDDEN);
        }

        String randomNumberCard;
        int numeroCvv;
        do {
            randomNumberCard = Card.aleatoryNumberCards();
        } while(cardRepository.findByNumber(randomNumberCard) != null);

        do {
            numeroCvv = Card.aleatoryNumberCvv();
        } while(cardRepository.findByCvv(numeroCvv) != null);

        Client client = clientRespository.findByEmailAddress(authentication.getName());

        for (Card card : client.getCards()) {
            if (card.getType().equals(CardType.valueOf(type)) && card.getColor().equals(CardColor.valueOf(color))) {
                return new ResponseEntity<>("You already have this color and type of card", HttpStatus.FORBIDDEN);
            }
        }

        Card newCard = new Card(CardType.valueOf(type), CardColor.valueOf(color), randomNumberCard , numeroCvv , LocalDate.now() , LocalDate.now().plusYears(5));
        clientRespository.findByEmailAddress(authentication.getName()).addCard(newCard);
        cardRepository.save(newCard);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
