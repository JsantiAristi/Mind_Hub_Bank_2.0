package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.AccountType;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ClientController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;

    // Servlets
    @GetMapping("/api/clients")
    public List<ClientDTO> getClients(){
       return clientService.getClientsDTO();
    }

    @GetMapping("/api/clients/current")
    public ClientDTO getClient (Authentication authentication) {
        return clientService.getClientDTO(authentication);
    }

    @PostMapping("/api/clients")
       public ResponseEntity<Object> register(
               @RequestParam String firstName, @RequestParam String lastName,
               @RequestParam String emailAddress, @RequestParam String password) {
//         FirstName parameter
           if ( firstName.isBlank() || !firstName.matches("^[a-zA-Z]*$") ) {
               return new ResponseEntity<>("Please enter a valid firstName. Only letters are allowed.", HttpStatus.FORBIDDEN);}
//        LastName parameter
           if ( lastName.isBlank() || !lastName.matches("^[a-zA-Z]*$") ) {
               return new ResponseEntity<>("Please enter a valid lastName. Only letters are allowed.", HttpStatus.FORBIDDEN);}
//        emailAddress parameter
           if ( emailAddress.isBlank() || !emailAddress.contains("@") ) {
               return new ResponseEntity<>("Please enter a valid email address.", HttpStatus.FORBIDDEN);}
//        Password parameter
           if ( password.isBlank()) {
                return new ResponseEntity<>("Password required", HttpStatus.FORBIDDEN);}
//        Email in use in our DB
           if ( clientService.getClientWithEmail(emailAddress) != null) {
               return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);}
//         Create de new client and a new account
                Client newClient = new Client(firstName, lastName, emailAddress, "../../assets/chico.png" , passwordEncoder.encode(password));
                clientService.saveClient(newClient);
                Account newAccount = new Account(AccountType.SAVING,accountService.aleatoryNumberNotRepeat(), LocalDateTime.now(), 0.00, true);
                newClient.addAccount(newAccount);
                accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);
       }

    @PutMapping("/api/clients")
    public ResponseEntity<Object> changeInfo(Authentication authentication, @RequestBody Client client){

        Client clientAuthenticated = clientService.getClientAuthenticated(authentication);

        if( clientAuthenticated != null ){
            if( !client.getFirstName().equals(clientAuthenticated.getFirstName())) {
                clientAuthenticated.setFirstName(client.getFirstName());
                clientService.saveClient(clientAuthenticated);
                return new ResponseEntity<>("First Name changed" , HttpStatus.ACCEPTED);
            } else if (!client.getLastName().equals(clientAuthenticated.getLastName())) {
                clientAuthenticated.setLastName(client.getLastName());
                clientService.saveClient(clientAuthenticated);;
                return new ResponseEntity<>("Last Name changed" , HttpStatus.ACCEPTED);
            } else if (!client.getEmailAddress().equals(clientAuthenticated.getEmailAddress())) {
                clientAuthenticated.setEmailAddress(client.getEmailAddress());
                clientService.saveClient(clientAuthenticated);
                return new ResponseEntity<>("Email Address changed" , HttpStatus.ACCEPTED);
            } else if (!client.getImage().equals(clientAuthenticated.getImage())) {
                clientAuthenticated.setImage(client.getImage());
                clientService.saveClient(clientAuthenticated);
                return new ResponseEntity<>("Picture changed" , HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>("Not changes", HttpStatus.ACCEPTED);}
        } else {
            return new ResponseEntity<>("This client doesn't exist" , HttpStatus.BAD_REQUEST);}
    }
}
