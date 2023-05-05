package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRespository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
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
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

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

        Client client = clientService.getClientAuthenticated(authentication);

        if (client == null){
            return new ResponseEntity<>("You can´t create an account, because you are not a client", HttpStatus.FORBIDDEN);
        };

        if (client.getAccounts().size() <= 2){
            Account newAccount = new Account(accountService.aleatoryNumberNotRepeat(), LocalDateTime.now(), 0.00);
            clientRespository.findByEmailAddress(authentication.getName()).addAccount(newAccount);
            accountRepository.save(newAccount);
        } else {
            return new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
