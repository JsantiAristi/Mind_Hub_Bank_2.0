package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
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
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@RestController
public class AccountController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    // Servlets
    @GetMapping("/api/clients/current/accounts")
    public List<AccountDTO> getAccounts (Authentication authentication) {
        return accountService.getAccountsDTO(authentication);
    }

    @GetMapping("/api/clients/current/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> newAccount (Authentication authentication, @RequestParam String accountType) {

        Client client = clientService.getClientAuthenticated(authentication);
        List<Account> accountsActive = client.getAccounts().stream().filter(Account::isActive).collect(toList());
        Set<Account> accounts = client.getAccounts();

        if (client == null){
            return new ResponseEntity<>("You can´t create an account, because you are not a client", HttpStatus.FORBIDDEN);}

        if ( !accountType.equalsIgnoreCase("SAVING") && !accountType.equalsIgnoreCase("CURRENT")){
            return new ResponseEntity<>("Please select a correct type of account.", HttpStatus.FORBIDDEN);}

        if ( accountsActive.size() <= 2 && accounts.size() <= 15 ){
            Account newAccount = new Account(AccountType.valueOf(accountType.toUpperCase()), accountService.aleatoryNumberNotRepeat(), LocalDateTime.now(), 0.00, true);
            clientService.getClientAuthenticated(authentication).addAccount(newAccount);
            accountService.saveAccount(newAccount);
        } else {
            return new ResponseEntity<>("You reached the limit of accounts", HttpStatus.FORBIDDEN);}

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> InactiveAccount (
            Authentication authentication , @RequestParam Long idAccount){

        Client client = clientService.getClientAuthenticated(authentication);
        Account account = accountService.getAccountByID(idAccount);

        if( account == null ){
            return new ResponseEntity<>("this card doesn't exist", HttpStatus.FORBIDDEN);
        } else if ( !account.isActive() ){
            return new ResponseEntity<>("this card is already inactive", HttpStatus.FORBIDDEN);
        } else if( account.getBalance() > 0 ){
            return new ResponseEntity<>("You can't delete this account, because you have money", HttpStatus.FORBIDDEN);
        }

        if (client == null) {
            return new ResponseEntity<>("You are not a client", HttpStatus.FORBIDDEN);
        }else if( client.getAccounts().stream().filter(account1 -> account1.getId() == idAccount).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("this card is not yours", HttpStatus.FORBIDDEN);}

        account.setActive(false);
        account.getTransactions().stream().forEach( transaction -> transaction.setActive(false));
        accountService.saveAccount(account);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
