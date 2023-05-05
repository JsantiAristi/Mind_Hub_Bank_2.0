package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;

public interface AccountService {
    void saveAccount(Account account);
    String aleatoryNumber();
    String aleatoryNumberNotRepeat();

}
