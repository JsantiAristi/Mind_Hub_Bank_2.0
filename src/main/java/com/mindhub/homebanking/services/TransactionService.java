package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    List<Transaction> getTransaction(LocalDateTime InitDate, LocalDateTime FinalDate, Account account);
}
