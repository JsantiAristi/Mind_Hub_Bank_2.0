package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
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
    public String aleatoryNumberCards() {
        String numberCard = "";
        String numberCardInit = "";
        for (int i = 0; i < 4; i++) {
            int min = 1000;
            int max = 8999;
            numberCardInit += (int) (Math.random() * max + min) + "-";
        }
        numberCard = numberCardInit.substring(0 , numberCardInit.length() - 1);
        return numberCard;
    }

    @Override
    public String aleatoryNumberCardsNotRepeat() {
        String randomNumberCard;
        do {
            randomNumberCard = aleatoryNumberCards();
        } while(cardRepository.findByNumber(randomNumberCard) != null);
        return randomNumberCard;
    }

    @Override
    public int aleatoryNumberCvv() {
        int min = 100;
        int max = 899;
        return (int) (Math.random() * max + min);
    }
}
