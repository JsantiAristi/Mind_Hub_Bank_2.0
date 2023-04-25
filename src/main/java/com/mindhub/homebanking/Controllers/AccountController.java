package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private ClientRespository clientRespository;
    @Autowired
    private AccountRepository accountRepository;

    // Servlets
    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccounts (Authentication authentication) {
        return new ClientDTO(clientRespository.findByEmailAddress(authentication.getName())).getAccounts().stream().collect(toList());
    }

    @RequestMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountRepository.findById(id).map(account -> new AccountDTO(account)).orElse(null);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> newAccount (Authentication authentication) {
        String randomNumber;
        do {
            randomNumber = Account.aleatoryNumber();
        } while(accountRepository.findByNumber(randomNumber) != null);

        if (clientRespository.findByEmailAddress(authentication.getName()).getAccounts().size() <= 2){
            Account newAccount = new Account(randomNumber, LocalDateTime.now(), 0.00);
            clientRespository.findByEmailAddress(authentication.getName()).addAccount(newAccount);
            accountRepository.save(newAccount);
        } else {
            return new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
