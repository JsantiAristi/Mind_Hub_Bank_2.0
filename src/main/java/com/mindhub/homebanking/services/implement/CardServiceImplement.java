package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Utils.CardUtils;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientService clientService;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public List<CardDTO> getCardsDTO(Authentication authentication) {
        return new ClientDTO(clientService.getClientAuthenticated(authentication)).getCards().stream().collect(toList());
    }

    @Override
    public Card getCardByID(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card getCard(String number) {
        return cardRepository.findByNumber(number);
    }

    @Override
    public String aleatoryNumberCardsNotRepeat() {
        String randomNumberCard;
        do {
            randomNumberCard = CardUtils.getCards();
        } while(cardRepository.findByNumber(randomNumberCard) != null);
        return randomNumberCard;
    }
}
