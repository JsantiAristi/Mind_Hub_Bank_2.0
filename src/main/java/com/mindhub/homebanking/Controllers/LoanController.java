package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Utils.LoanUtil;
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

    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoanDTO();
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> newLoan(Authentication authentication , @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.getClientAuthenticated(authentication);
        Loan loan = loanService.loanById(loanApplicationDTO.getId());
        Account accountAuthenticated = accountService.getAccountAuthenticatedDTO(loanApplicationDTO);
        String description = loan.getName() + " loan approve";

//      loan and ID property
        if ( loan == null ){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);}
//      amount property
        if (loanApplicationDTO.getAmount().isNaN() || loanApplicationDTO.getAmount() < 1) {
            return new ResponseEntity<>("Please enter an amount of loan bigger than 0", HttpStatus.FORBIDDEN);
        } else if( loanApplicationDTO.getAmount() > loan.getMaxAmount() ){
            return new ResponseEntity<>("You can't request a value bigger than the original loan", HttpStatus.FORBIDDEN);
        }
//       payments property
        if ( !loan.getPayments().contains(loanApplicationDTO.getPayments()) ) {
            return new ResponseEntity<>("Please verify that you enter a valid number of payments of the Loan that you want to select", HttpStatus.FORBIDDEN);}
//      account property
        if (loanApplicationDTO.getAccount().isBlank()) {
            return new ResponseEntity<>("Please enter the account that you want to transfer the loan", HttpStatus.FORBIDDEN);
        } else if ( accountAuthenticated == null ) {
            return new ResponseEntity<>("This account doesn't exist", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(loanApplicationDTO.getAccount())).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}

        for (ClientLoan clientLoan : client.getClientLoans()) {
            if (clientLoan.getLoan().getName().equalsIgnoreCase(loan.getName()) && clientLoan.getFinalAmount() > 0) {
                return new ResponseEntity<>("You already have a " + loan.getName() + " loan in your account", HttpStatus.FORBIDDEN);
            }
        }

        ClientLoan clientLoan = new ClientLoan( loanApplicationDTO.getAmount(), loanApplicationDTO.getAmount() * loan.getInterest(), loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() + loanApplicationDTO.getAmount() );

        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), description , LocalDateTime.now(), true, accountAuthenticated.getBalance());
        accountAuthenticated.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/api/current/loans")
    public ResponseEntity<Object> payLoan(Authentication authentication , @RequestParam Long idLoan , @RequestParam String account, @RequestParam Double amount) {

        Client client = clientService.getClientAuthenticated(authentication);
        ClientLoan clientLoan = clientLoanService.getClientLoan(idLoan);
        Account accountAuthenticated = accountService.getAccountAuthenticated(account);
        String description = "Pay " + clientLoan.getLoan().getName() + " loan";
//      id parameter
        if( clientLoan == null ){
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        } else if( client == null){
            return new ResponseEntity<>("You are not registered as a client", HttpStatus.FORBIDDEN);
        } else if( clientLoan.getFinalAmount() == 0 ){
            return new ResponseEntity<>("This loan is already paid", HttpStatus.FORBIDDEN);
        }
//        account parameter
        if ( account.isBlank() ){
            return new ResponseEntity<>("PLease enter an account", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(accounts -> accounts.getNumber().equalsIgnoreCase(account)).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN);}
//      amount parameter
        if ( amount < 1 ){
            return new ResponseEntity<>("PLease enter an amount bigger than 0", HttpStatus.FORBIDDEN);
        }  else if ( accountAuthenticated.getBalance() < amount ){
            return new ResponseEntity<>("Insufficient balance in your account " + accountAuthenticated.getNumber(), HttpStatus.FORBIDDEN);}

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() - amount );
        clientLoan.setFinalAmount( clientLoan.getFinalAmount() - amount);

        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description , LocalDateTime.now(), true, accountAuthenticated.getBalance());
        accountAuthenticated.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);

        if ( amount < clientLoan.getFinalAmount() ){
            clientLoan.setPayments(clientLoan.getPayments() - 1 );
        } else {
            clientLoan.setPayments(0);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/api/loans/manager")
    public ResponseEntity<Object> newLoanAdmin(@RequestBody Loan loan) {

        if (loan.getName().isBlank()){
            return new ResponseEntity<>("Please enter a name for the new loan", HttpStatus.FORBIDDEN);
        } else if ( loan.getDescriptionLoan().isBlank() ){
            return new ResponseEntity<>("Please enter a description for the new loan", HttpStatus.FORBIDDEN);
        } else if( loan.getMaxAmount() < 1 ){
            return new ResponseEntity<>("Please enter an amount bigger than 1", HttpStatus.FORBIDDEN);
        } else if ( loan.getPayments().size() == 0 ){
            return new ResponseEntity<>("Please enter a valid amount of payments", HttpStatus.FORBIDDEN);
        } else if ( loan.getInterest() < 1 ){
            return new ResponseEntity<>("Please enter an percentage of interest ", HttpStatus.FORBIDDEN);
        }

        for ( Loan loans : loanService.getLoan() ){
            if ( loan.getName().equalsIgnoreCase(loans.getName()) ){
                return new ResponseEntity<>("This type of loan " + loan.getName() + " is already used", HttpStatus.FORBIDDEN);}
        };

        Loan newLoan = new Loan(loan.getName(), loan.getMaxAmount() , loan.getPayments() , loan.getDescriptionLoan(), loan.getInterest());
        loanService.saveLoan(newLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}

//Agregar intereses dependiendo de las cuotas.
//Implementar servicio de pagos con tarjetas y postnet
