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
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

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

    @PostMapping("/api/clients/current/transactions/pdf")
    public ResponseEntity<Object> pdfTransaction (
            Authentication authentication , @RequestParam Long id, @RequestParam String initDate, @RequestParam String finalDate) throws IOException, DocumentException, ParseException {

        Client client = clientService.getClientAuthenticated(authentication);
        Account account = accountService.getAccountByID(id);
        Date initDate1 = TransactionUtils.stringtoDate(initDate);
        LocalDateTime initDate2 = TransactionUtils.dateToLocalDateTime(initDate1);

        Date finalDate1 = TransactionUtils.stringtoDateFinal(finalDate);
        LocalDateTime finalDate2 = TransactionUtils.dateToLocalDateTime(finalDate1).plusDays(1).minusSeconds(1);

        if(client == null){
            return new ResponseEntity<>(".", HttpStatus.FORBIDDEN);
        }

        if(client.getAccounts()
                .stream()
                .noneMatch(account1 -> account1.getNumber().equals(account.getNumber()))){
            return new ResponseEntity<>("This account doesn't belong to you", HttpStatus.FORBIDDEN);}

        if (initDate.isBlank()){
            return new ResponseEntity<>("Start date can't on blank", HttpStatus.FORBIDDEN);
        }else if(finalDate.isBlank()){
            return new ResponseEntity<>("End date can't be on blank", HttpStatus.FORBIDDEN);
        }

//        Trabajamos en el documento PDF
        Document document = new Document();
        PdfWriter.getInstance( document, new FileOutputStream( "transaction.pdf" ));
        document.open();
        Image logo = Image.getInstance("C:\\Users\\santi\\cursos programacion\\Mindhub\\Java\\task\\homebanking\\src\\main\\resources\\static\\assets\\insignia-del-sheriff.png");
        logo.scaleToFit(100, 100);
        document.add(logo);

        Font Title = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD);
        Paragraph title = new Paragraph("MINDHUB BROTHERS BANK", Title);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        title.setSpacingBefore(20);
        document.add(title);

        Font Title2 = new Font(Font.FontFamily.TIMES_ROMAN, 25, Font.BOLD);
        Paragraph title2 = new Paragraph("Hello " + client.getFirstName() +" "+ client.getLastName(), Title2);
        title2.setAlignment(Paragraph.ALIGN_CENTER);
        title2.setSpacingAfter(20);
        title2.setSpacingBefore(20);
        document.add(title2);

        Font SubTitle  = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Paragraph clientText = new Paragraph("Here are your transactions from " + initDate + " to " + finalDate + " from your account " + account.getNumber(), SubTitle);
        clientText.setAlignment(Paragraph.ALIGN_CENTER);
        clientText.setSpacingAfter(20);
        clientText.setSpacingBefore(20);
        document.add(clientText);

        Font InfoTitle  = new Font(Font.FontFamily.TIMES_ROMAN, 14);
        Paragraph typeAccount = new Paragraph("Account type: " + account.getAccountType() + ".", InfoTitle) ;
        typeAccount.setAlignment(Paragraph.ALIGN_LEFT);
        typeAccount.setSpacingAfter(5);
        typeAccount.setSpacingBefore(10);
        document.add(typeAccount);

        Paragraph balanceAccount = new Paragraph("Total balance: $" + NumberFormat.getNumberInstance(Locale.US).format(account.getBalance()), InfoTitle);
        balanceAccount.setAlignment(Paragraph.ALIGN_LEFT);
        balanceAccount.setSpacingAfter(5);
        balanceAccount.setSpacingBefore(10);
        document.add(balanceAccount);

        String creationDate = TransactionUtils.getStringDateFromLocalDateTime(account.getCreationDate());
        Paragraph creationCell = new Paragraph(("Creation Date: " + creationDate), InfoTitle);
        creationCell.setAlignment(Paragraph.ALIGN_LEFT);
        creationCell.setSpacingAfter(20);
        creationCell.setSpacingBefore(10);
        document.add(creationCell);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        PdfPCell cellAmount = new PdfPCell( new Paragraph("Amount"));
        cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellAmount);

        PdfPCell cellDate = new PdfPCell(new Paragraph("Date"));
        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDate);

        PdfPCell cellDescription = new PdfPCell(new Paragraph("Description"));
        cellDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDescription);

        PdfPCell cellType = new PdfPCell(new Paragraph("Type"));
        cellType.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellType);

        PdfPCell cellTime = new PdfPCell(new Paragraph("Time"));
        cellTime.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellTime);

        PdfPCell cellBalance = new PdfPCell(new Paragraph("Balance"));
        cellBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellBalance);

        for ( Transaction transaction : transactionService.getTransaction( initDate2,  finalDate2, account) ){
            String date = TransactionUtils.getStringDateFromLocalDateTime(transaction.getDate());
            String hour = TransactionUtils.getStringHourFromLocalDateTime(transaction.getDate());

            PdfPCell cellTransactionAmount = new PdfPCell(new Paragraph(
                    transaction.getType().name().equals("DEBIT")? "$ -" + (NumberFormat.getNumberInstance(Locale.US).format(transaction.getAmount())) : "$" + NumberFormat.getNumberInstance(Locale.US).format(transaction.getAmount())));
            if ( transaction.getType().name().equals("DEBIT") ){
                cellTransactionAmount.setBackgroundColor(BaseColor.RED);
            } else {
                cellTransactionAmount.setBackgroundColor(BaseColor.GREEN);
            }
            cellTransactionAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionAmount.setFixedHeight(50);
            table.addCell(cellTransactionAmount);

            PdfPCell cellTransactionDate = new PdfPCell(new Paragraph(date));
            cellTransactionDate.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionDate.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionDate.setFixedHeight(50);
            table.addCell(cellTransactionDate);

            PdfPCell cellTransactionDescription = new PdfPCell(new Paragraph(transaction.getDescription()));
            cellTransactionDescription.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionDescription.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionDescription.setFixedHeight(50);
            table.addCell(cellTransactionDescription);

            PdfPCell cellTransactionType = new PdfPCell(new Paragraph(transaction.getType().name()));
            cellTransactionType.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionType.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionType.setFixedHeight(50);
            table.addCell(cellTransactionType);

            PdfPCell cellTransactionTime = new PdfPCell(new Paragraph(hour));
            cellTransactionTime.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionTime.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionTime.setFixedHeight(50);
            table.addCell(cellTransactionTime);

            PdfPCell cellTransactionBalance = new PdfPCell(new Paragraph("$"+NumberFormat.getNumberInstance(Locale.US).format(transaction.getBalance())));
            cellTransactionBalance.setHorizontalAlignment(Element.ALIGN_CENTER);
            cellTransactionBalance.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cellTransactionBalance.setFixedHeight(50);
            table.addCell(cellTransactionBalance);

        };
        document.add(table);
        document.close();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
