package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.dtos.CardDTO;
import org.springframework.security.core.Authentication;
import java.util.List;

public interface CardService {
    void saveCard(Card card);
    List<CardDTO> getCardsDTO (Authentication authentication);
    Card getCardByID (Long id);
    Card getCard(String number);
    String aleatoryNumberCardsNotRepeat();
}
