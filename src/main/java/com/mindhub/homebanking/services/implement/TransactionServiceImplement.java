package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransaction(LocalDateTime InitDate, LocalDateTime FinalDate, Account account) {
        return transactionRepository.findByAccountBetweenDate(InitDate , FinalDate, account);
    }
}
