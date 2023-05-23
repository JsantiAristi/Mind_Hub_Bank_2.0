package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Card;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    void saveAccount(Account account);
    List<AccountDTO> getAccountsDTO(Authentication authentication);
    AccountDTO getAccountDTO(Long id);
    Account getAccountByID (Long id);
    Account getAccountAuthenticated(String number);
    Account getAccountAuthenticatedDTO(LoanApplicationDTO loanApplicationDTO);
    String aleatoryNumberNotRepeat();

}
