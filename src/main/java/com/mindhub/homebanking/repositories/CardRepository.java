package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.Models.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findByNumber(String number);
}
