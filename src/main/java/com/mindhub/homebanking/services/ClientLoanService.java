package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.ClientLoan;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientLoanDTO;

public interface ClientLoanService {
    void saveClientLoan(ClientLoan clientLoan);
    ClientLoan getClientLoan(Long id);
}
