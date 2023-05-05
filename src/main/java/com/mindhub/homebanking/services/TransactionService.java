package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;

public interface TransactionService {
    void saveTransaction(Transaction transaction);
}
