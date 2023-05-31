package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.PaymentDTO;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

@CrossOrigin(origins = {"*"})
@RestController
public class CardController {
    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getCards (Authentication authentication) {
        return cardService.getCardsDTO(authentication);
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> addCard (
            Authentication authentication , @RequestParam String type, @RequestParam String color){

        Client client = clientService.getClientAuthenticated(authentication);
        Set<Card> cards = client.getCards();

        if (client == null){
            return new ResponseEntity<>("You can´t create a card, because you are not a client", HttpStatus.FORBIDDEN);};
        if ( !type.equalsIgnoreCase("CREDIT")  && !type.equalsIgnoreCase("DEBIT")) {
            return new ResponseEntity<>(type + " is an incorrect type of card", HttpStatus.FORBIDDEN);}
        if ( !color.equalsIgnoreCase("TITANIUM") && !color.equalsIgnoreCase("GOLD") && !color.equalsIgnoreCase("SILVER")) {
            return new ResponseEntity<>(color + " is an incorrect color of card", HttpStatus.FORBIDDEN);}

        for (Card card : client.getCards()) {
            if (card.getType().equals(CardType.valueOf(type.toUpperCase())) && card.getColor().equals(CardColor.valueOf(color.toUpperCase())) && card.isActive()) {
                return new ResponseEntity<>("You already have a " + type.toLowerCase() + " " + color.toLowerCase() + " card", HttpStatus.FORBIDDEN);
            }
        }

        if ( cards.size() <= 15){
            Card newCard = new Card(CardType.valueOf(type.toUpperCase()), CardColor.valueOf(color.toUpperCase()), cardService.aleatoryNumberCardsNotRepeat() , CardUtils.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5), true);
            client.addCard(newCard);
            cardService.saveCard(newCard);
        } else {
            return new ResponseEntity<>("You can't request more than 15 cards, please contact us", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/clients/current/cards")
    public ResponseEntity<Object> InactiveCard (
            Authentication authentication , @RequestParam Long idCard){

        Client client = clientService.getClientAuthenticated(authentication);
        Card card = cardService.getCardByID(idCard);

        if( card == null ){
            return new ResponseEntity<>("this card doesn't exist", HttpStatus.FORBIDDEN);
        } else if ( !card.isActive() ){
            return new ResponseEntity<>("this card is already inactive", HttpStatus.FORBIDDEN);}

        if (client == null) {
            return new ResponseEntity<>("You are not a client", HttpStatus.FORBIDDEN);
        }else if( client.getCards().stream().filter(card1 -> card1.getId() == idCard).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("this card is not yours", HttpStatus.FORBIDDEN);}

        card.setActive(false);
        cardService.saveCard(card);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/clients/current/cards/postnet")
    public ResponseEntity<Object> payCards(@RequestBody PaymentDTO paymentDTO){

        Card clientCard = cardService.getCard( paymentDTO.getNumber() );
        Client client = clientService.getClientWithEmail(paymentDTO.getEmail());
        Account account = client.getAccounts().stream().filter( account1 -> account1.getBalance() >= paymentDTO.getAmount() ).collect(toList()).get(0);
        String description = "Payment with the card " + paymentDTO.getNumber();

        if (client == null){
            return new ResponseEntity<>("You are not a client, please create an account", HttpStatus.FORBIDDEN);
        } else if ( account == null ){
            return new ResponseEntity<>("You don't have money in your accounts", HttpStatus.FORBIDDEN);
        } else if (  client.getCards().stream().filter( card1 -> card1.getNumber().equalsIgnoreCase(paymentDTO.getNumber())).collect(toList()).size() == 0  ) {
            return new ResponseEntity<>("The card that you are trying to put, is not yours", HttpStatus.FORBIDDEN);}

        if ( clientCard.getCvv() != paymentDTO.getCvv() ){
            return new ResponseEntity<>("This cvv is not the same for the card " + paymentDTO.getNumber(), HttpStatus.FORBIDDEN);
        } else if ( !clientCard.getType().equals(paymentDTO.getType()) ){
            return new ResponseEntity<>("This card is not a " + paymentDTO.getType() + " card", HttpStatus.FORBIDDEN);
        } else if (!clientCard.getColor().equals(paymentDTO.getColor()) ) {
            return new ResponseEntity<>("This card is not a " + paymentDTO.getColor() + " card", HttpStatus.FORBIDDEN);
        } else if ( account.getBalance() < paymentDTO.getAmount() ){
            return new ResponseEntity<>("You don't have enough money in your accounts ", HttpStatus.FORBIDDEN);
        }

        account.setBalance(account.getBalance() - paymentDTO.getAmount());

        //      Añadir transacciones
        Transaction newTransaction = new Transaction(TransactionType.DEBIT, paymentDTO.getAmount(), description, LocalDateTime.now(),true, account.getBalance());
        account.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
