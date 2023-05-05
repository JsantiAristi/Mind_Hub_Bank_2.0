package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public String aleatoryNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 899999;
        return ("VIN-" + random.nextInt(max + min));
    }

    @Override
    public String aleatoryNumberNotRepeat() {
        String randomNumber;
        do {
            randomNumber = aleatoryNumber();
        } while (accountRepository.findByNumber(randomNumber) != null);
        return randomNumber;
    }
}
