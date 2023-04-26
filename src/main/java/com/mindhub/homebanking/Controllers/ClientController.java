package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static java.util.stream.Collectors.toList;

@RestController
public class ClientController {

    @Autowired
    private ClientRespository clientRespository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    // Servlets
    @RequestMapping("/api/clients")
    public List<ClientDTO> getClients() {
        return clientRespository.findAll().stream().map(client -> new ClientDTO(client)).collect(toList());
    }

    @RequestMapping("/api/clients/current")
    public ClientDTO getClient (Authentication authentication) {
        return new ClientDTO(clientRespository.findByEmailAddress(authentication.getName()));
    }

    @PostMapping("/api/clients")
       public ResponseEntity<Object> register(
               @RequestParam String firstName, @RequestParam String lastName,
               @RequestParam String emailAdress, @RequestParam String password) {
           if ( firstName.isBlank() || lastName.isBlank() || emailAdress.isBlank() || password.isBlank()) {
               return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
           }
           if (clientRespository.findByEmailAddress(emailAdress) != null) {
               return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
           }

           String randomNumber;
           do {
               randomNumber = Account.aleatoryNumber();
           } while(accountRepository.findByNumber(randomNumber) != null);

           Client newClient = new Client(firstName, lastName, emailAdress, passwordEncoder.encode(password));
           clientRespository.save(newClient);
           Account newAccount = new Account(randomNumber, LocalDateTime.now(), 0.00);
           newClient.addAccount(newAccount);
           accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
       }
}
