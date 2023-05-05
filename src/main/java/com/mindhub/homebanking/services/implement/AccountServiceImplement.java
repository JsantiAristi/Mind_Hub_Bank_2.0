package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Random;
import static java.util.stream.Collectors.toList;

@Service
public class AccountServiceImplement implements AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientService clientService;

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public List<AccountDTO> getAccountsDTO(Authentication authentication) {
        return new ClientDTO(clientService.getClientAuthenticated(authentication)).getAccounts().stream().collect(toList());
    }

    @Override
    public AccountDTO getAccountDTO(Long id) {
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @Override
    public Account getAccountAuthenticated(String number) {
        return accountRepository.findByNumber(number.toUpperCase());
    }

    @Override
    public Account getAccountAuthenticatedDTO(LoanApplicationDTO loanApplicationDTO) {
        return accountRepository.findByNumber(loanApplicationDTO.getAccount().toUpperCase());
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
