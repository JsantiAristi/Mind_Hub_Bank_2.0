package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.Utils.TransactionUtils;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import static java.util.stream.Collectors.toList;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class TransactionController {
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @Transactional
    @PostMapping("/api/clients/current/transactions")
    public ResponseEntity<Object> newTransaction (
            Authentication authentication , @RequestParam Double amount, @RequestParam String description,
            @RequestParam String initialAccount, @RequestParam String destinateAccount) {

        Client client = clientService.getClientAuthenticated(authentication);
        Account accountAuthenticated = accountService.getAccountAuthenticated(initialAccount);
        Account destinateAccountAuthenticated = accountService.getAccountAuthenticated(destinateAccount);

//      Amount parameter.
        if ( amount == null || amount.isNaN() ) {
            return new ResponseEntity<>("Please enter an amount.", HttpStatus.FORBIDDEN);
        } else if( amount < 1 ){
            return new ResponseEntity<>("Please enter an amount bigger than 0.", HttpStatus.FORBIDDEN);
        } else if ( accountAuthenticated.getBalance() < amount ){
            return new ResponseEntity<>("Insufficient balance", HttpStatus.FORBIDDEN); }
//      Description parameter.
        if ( description.isBlank() ) {
            description = "Transaction to " + destinateAccount; }
//      initialAccount Parameter.
        if ( initialAccount.isBlank()){
            return new ResponseEntity<>("Please enter one of your accounts", HttpStatus.FORBIDDEN);
        } else if ( accountAuthenticated == null) {
            return new ResponseEntity<>("This account " + initialAccount + " doesn't exist", HttpStatus.FORBIDDEN);
        } else if ( client.getAccounts().stream().filter(account -> account.getNumber().equalsIgnoreCase(initialAccount)).collect(toList()).size() == 0 ){
            return new ResponseEntity<>("This account is not yours.", HttpStatus.FORBIDDEN); }
//      destinateAccount Parameter.
        if ( destinateAccount.isBlank() ){
            return new ResponseEntity<>("Please enter an account that you want to transfer the money", HttpStatus.FORBIDDEN);
        } else if ( destinateAccountAuthenticated == null ){
            return new ResponseEntity<>("This account " + destinateAccount + " doesn't exist", HttpStatus.FORBIDDEN);
        } else if (destinateAccount.equalsIgnoreCase(initialAccount)) {
            return new ResponseEntity<>("You can't send money to the same account number", HttpStatus.FORBIDDEN); }
        //  Restar o sumar valores a los balances.
        accountAuthenticated.setBalance( accountAuthenticated.getBalance() - amount );
        destinateAccountAuthenticated.setBalance( destinateAccountAuthenticated.getBalance() + amount );
//      Añadir transacciones
        Transaction newTransaction = new Transaction(TransactionType.DEBIT, amount, description, LocalDateTime.now(),true, accountAuthenticated.getBalance());
        accountAuthenticated.addTransaction(newTransaction);
        transactionService.saveTransaction(newTransaction);
        Transaction newTransaction2 = new Transaction(TransactionType.CREDIT, amount, Transaction.stringToAccount(initialAccount.toUpperCase()), LocalDateTime.now() ,true, destinateAccountAuthenticated.getBalance());
        destinateAccountAuthenticated.addTransaction(newTransaction2);
        transactionService.saveTransaction(newTransaction2);


        return new ResponseEntity<>(HttpStatus.CREATED);
    };

//    Copia de Ivan (Indifference 2) :D
    @PostMapping("/api/clients/current/transactions/pdf")
    public ResponseEntity<Object> pdfTransaction (
            Authentication authentication , @RequestParam Long id, @RequestParam String initDate, @RequestParam String finalDate) throws FileNotFoundException, DocumentException, ParseException {

        Client client = clientService.getClientAuthenticated(authentication);
        Account account = accountService.getAccountByID(id);
        Date initDate1 = TransactionUtils.stringtoDate(initDate);
        LocalDateTime initDate2 = TransactionUtils.dateToLocalDateTime(initDate1);

        Date finalDate1 = TransactionUtils.stringtoDate(finalDate);
        LocalDateTime finalDate2 = TransactionUtils.dateToLocalDateTime(finalDate1);

        if(client == null){
            return new ResponseEntity<>(".", HttpStatus.FORBIDDEN);
        }

        Document document = new Document();
        PdfWriter.getInstance( document, new FileOutputStream( "pdf.pdf" ));

//        Trabajamos en el documento
        document.open();
        PdfPTable table = new PdfPTable(1);
        PdfPCell cell1 = new PdfPCell( new Paragraph("Amount"));
        table.addCell(cell1);

        for ( Transaction transaction : transactionService.getTransaction( initDate2,  finalDate2, account) ){
            PdfPCell cellAmount = new PdfPCell( new Paragraph( Double.toString( transaction.getAmount())));
            table.addCell(cellAmount);
        };
        document.add(table);
        document.close();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
