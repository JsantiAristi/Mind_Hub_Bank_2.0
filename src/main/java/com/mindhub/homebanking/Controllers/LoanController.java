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
    @Autowired
    ClientLoanRepository clientLoanRepository;
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
    public ResponseEntity<Object> newLoan(Authentication authentication , @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientRespository.findByEmailAddress(authentication.getName());
        Loan loan = loanRepository.findById(loanApplicationDTO.getId()).orElse(null);
        Account accountAuthenticated = accountRepository.findByNumber(loanApplicationDTO.getAccount().toUpperCase());

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
            if (clientLoan.getLoan().getName().equalsIgnoreCase(loan.getName()) ) {
                return new ResponseEntity<>("You already have a " + loan.getName() + " loan in your account", HttpStatus.FORBIDDEN);
            }
        }

        ClientLoan clientLoan = new ClientLoan( loanApplicationDTO.getAmount() + loanApplicationDTO.getAmount()*0.2 , loanApplicationDTO.getAmount(), loanApplicationDTO.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanRepository.save(clientLoan);

        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName() , LocalDateTime.now());
        accountAuthenticated.addTransaction(newTransaction);
        transactionRepository.save(newTransaction);

        accountAuthenticated.setBalance( accountAuthenticated.getBalance() + loanApplicationDTO.getAmount() );

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


