package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public class LoanController {
    @Autowired
    LoanRepository loanRepository;
//    @Autowired
//    LoanApplicationRepository loanApplicationRepository;
    @Autowired
    ClientRespository clientRespository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @RequestMapping("/api/loans")
    public List<LoanDTO> getClients() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }
    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication , @RequestBody LoanApplicationDTO loanApplicationDTO){

        Client client = clientRespository.findByEmailAddress(authentication.getName());
        Account accountAuthenticated = accountRepository.findByNumber(loanApplicationDTO.getAccount().toUpperCase());

//      name property
        if ( !loanApplicationDTO.getName().equalsIgnoreCase("Mortgage") || !loanApplicationDTO.getName().equalsIgnoreCase("Personal") || !loanApplicationDTO.getName().equalsIgnoreCase("Automotive") ){
            return new ResponseEntity<>("Please enter a valid name of loan", HttpStatus.FORBIDDEN);}
//      amount property
        if ( loanApplicationDTO.getAmount().isNaN() ){
            return new ResponseEntity<>("Please enter an amount of loan", HttpStatus.FORBIDDEN);}
//       payments property
        if ( loanApplicationDTO.getPayments() < 1 ){
            return new ResponseEntity<>("Please enter the payments of the loan", HttpStatus.FORBIDDEN);}
//      account property
        if ( loanApplicationDTO.getAccount().isBlank() ){
            return new ResponseEntity<>("Please enter the account that you want to transfer the loan", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(loanApplicationDTO.getAccount())).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}

        Transaction newTransaction2 = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loanApplicationDTO.getName() , LocalDateTime.now());
        accountAuthenticated.addTransaction(newTransaction2);
        transactionRepository.save(newTransaction2);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


