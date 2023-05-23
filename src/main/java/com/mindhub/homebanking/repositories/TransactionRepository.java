package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByType(TransactionType type);
    @Query("Select T from Transaction T where T.account = :account AND T.date >= :InitDate AND T.date <= :FinalDate")
    List<Transaction> findByAccountBetweenDate(LocalDateTime InitDate, LocalDateTime FinalDate, Account account);
}
