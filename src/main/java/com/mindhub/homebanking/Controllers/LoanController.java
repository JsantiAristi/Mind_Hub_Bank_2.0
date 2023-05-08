package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class LoanController {
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    LoanService loanService;
    @Autowired
    ClientLoanService clientLoanService;

    @RequestMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoanDTO();
    }
    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication , @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.getClientAuthenticated(authentication);
        Optional<Loan> loan = loanService.loanById(loanApplicationDTO.getId());
        Account accountAuthenticated = accountService.getAccountAuthenticatedDTO(loanApplicationDTO);
        String description = loan.get().getName() + " loan approve";

//      loan and ID property
        if ( loan.isEmpty() ){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);}
//      amount property
        if (loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getAmount() < 1) {
            return new ResponseEntity<>("Please enter an amount of loan bigger than 0", HttpStatus.FORBIDDEN);
        } else if( loanApplicationDTO.getAmount() > loan.get().getMaxAmount() ){
            return new ResponseEntity<>("You can't request a value bigger than the original loan", HttpStatus.FORBIDDEN);
        }
//       payments property
        if ( !loan.get().getPayments().contains(loanApplicationDTO.getPayments()) ) {
            return new ResponseEntity<>("Please verify that you enter a valid number of payments of the Loan that you want to select", HttpStatus.FORBIDDEN);}
//      account property
        if (loanApplicationDTO.getAccount().isBlank()) {
            return new ResponseEntity<>("Please enter the account that you want to transfer the loan", HttpStatus.FORBIDDEN);
        } else if ( accountAuthenticated == null ) {
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(loanApplicationDTO.getAccount())).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}

        for (ClientLoan clientLoan : client.getClientLoans()) {
            if (clientLoan.getLoan().getName().equalsIgnoreCase(loan.get().getName()) ) {
                return new ResponseEntity<>("You already have a " + loan.get().getName() + " loan in your account", HttpStatus.FORBIDDEN);
            }
        }

        ClientLoan clientLoan = new ClientLoan( loanApplicationDTO.getAmount(), loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount()*0.2 , loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.get().addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), description , LocalDateTime.now());
        accountAuthenticated.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() + loanApplicationDTO.getAmount() );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/current/loans")
    public ResponseEntity<Object> payLoan(Authentication authentication , @RequestParam Long id , @RequestParam String account, @RequestParam Double amount) {

        Client client = clientService.getClientAuthenticated(authentication);
        ClientLoan clientLoan = clientLoanService.getClientLoan(id);
        Account accountAuthenticated = accountService.getAccountAuthenticated(account);
        String description = "Pay Loan";

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description , LocalDateTime.now());
        accountAuthenticated.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() - amount );
        clientLoan.setFinalAmount( clientLoan.getFinalAmount() - amount);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


