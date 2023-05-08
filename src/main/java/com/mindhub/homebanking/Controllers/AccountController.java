package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    // Servlets
    @RequestMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccounts (Authentication authentication) {
        return accountService.getAccountsDTO(authentication);
    }

    @RequestMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> newAccount (Authentication authentication) {

        Client client = clientService.getClientAuthenticated(authentication);

        if (client == null){
            return new ResponseEntity<>("You can´t create an account, because you are not a client", HttpStatus.FORBIDDEN);
        };

        if (client.getAccounts().size() <= 2){
            Account newAccount = new Account(accountService.aleatoryNumberNotRepeat(), LocalDateTime.now(), 0.00);
            clientService.getClientAuthenticated(authentication).addAccount(newAccount);
            accountService.saveAccount(newAccount);
        } else {
            return new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
