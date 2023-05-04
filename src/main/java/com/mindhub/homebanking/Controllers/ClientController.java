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
//         FirstName parameter
           if ( firstName.isBlank() || !firstName.matches("^[a-zA-Z]*$") ) {
               return new ResponseEntity<>("Please enter a valid firstName. Only letters are allowed.", HttpStatus.FORBIDDEN);
           }
//        LastName parameter
           if ( lastName.isBlank() || !lastName.matches("^[a-zA-Z]*$") ) {
               return new ResponseEntity<>("Please enter a valid lastName. Only letters are allowed.", HttpStatus.FORBIDDEN);
           }
//        emailAdress parameter
           if ( emailAdress.isBlank() || !emailAdress.contains("@") ) {
               return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.FORBIDDEN);
           }
//        Password parameter
           if ( password.isBlank()) {
                return new ResponseEntity<>("Password required", HttpStatus.FORBIDDEN);}
//        Email in use in our DB
           if (clientRespository.findByEmailAddress(emailAdress) != null) {
               return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);}
//         Create de new client and a new account
                String randomNumber;
                do {
                    randomNumber = Account.aleatoryNumber();
                } while (accountRepository.findByNumber(randomNumber) != null);

                Client newClient = new Client(firstName, lastName, emailAdress, "../../assets/chico.png" , passwordEncoder.encode(password));
                clientRespository.save(newClient);
                Account newAccount = new Account(randomNumber, LocalDateTime.now(), 0.00);
                newClient.addAccount(newAccount);
                accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
       }

    @PutMapping("/api/clients")
    public ResponseEntity<Object> changeInfo(Authentication authentication, @RequestBody Client client){

        Client clientAutenticated = clientRespository.findByEmailAddress(authentication.getName());

        if( clientAutenticated != null ){
            if( !client.getFirstName().equals(clientAutenticated.getFirstName())) {
                clientAutenticated.setFirstName(client.getFirstName());
                clientRespository.save(clientAutenticated);
                return new ResponseEntity<>("First Name changed" , HttpStatus.ACCEPTED);
            } else if (!client.getLastName().equals(clientAutenticated.getLastName())) {
                clientAutenticated.setLastName(client.getLastName());
                clientRespository.save(clientAutenticated);
                return new ResponseEntity<>("Last Name changed" , HttpStatus.ACCEPTED);
            } else if (!client.getEmailAddress().equals(clientAutenticated.getEmailAddress())) {
                clientAutenticated.setEmailAddress(client.getEmailAddress());
                clientRespository.save(clientAutenticated);
                return new ResponseEntity<>("Email Address changed" , HttpStatus.ACCEPTED);
            } else if (!client.getImage().equals(clientAutenticated.getImage())) {
                clientAutenticated.setImage(client.getImage());
                clientRespository.save(clientAutenticated);
                return new ResponseEntity<>("Picture changed" , HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Not changes", HttpStatus.ACCEPTED);
            }
        } else {
            return new ResponseEntity<>("This client doesn't exist" , HttpStatus.BAD_REQUEST);
        }
    }
}
