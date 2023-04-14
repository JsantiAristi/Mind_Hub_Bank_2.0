package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRespository clientRepository , AccountRepository accountRepository , TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "Morel" , "melba@mindhub.com");
			clientRepository.save(client1);
			Client client2 = new Client("Santiago" , "Aristizabal" , "jsanti@gmail.com");
			clientRepository.save(client2);
			Client client3 = new Client("Carlos" , "Hinestrosa" , "Carlos12@yahoo.com");
			clientRepository.save(client3);

			Account account1 = new Account("VIN001" , LocalDateTime.now() , 5000.00);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN002" , LocalDateTime.now().plusDays(1) , 7500.00);
			client1.addAccount(account2);
			accountRepository.save(account2);
			Account account3 = new Account("VIN003" , LocalDateTime.now().plusDays(2) , 6000.50);
			client2.addAccount(account3);
			accountRepository.save(account3);
			Account account4 = new Account("VIN004", LocalDateTime.now() , 500.50);
			client3.addAccount(account4);
			accountRepository.save(account4);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT , 1000.00 , "Videogames" , LocalDateTime.now());
			account1.addTransaction(transaction1);
			transactionRepository.save(transaction1);
			Transaction transaction2 = new Transaction(TransactionType.CREDIT , 2000.00 , "Alcohol" , LocalDateTime.now().plusDays(2));
			account2.addTransaction(transaction2);
			transactionRepository.save(transaction2);
			Transaction transaction3 = new Transaction(TransactionType.DEBIT , 1000.00 , "Food" , LocalDateTime.now().plusDays(1));
			account3.addTransaction(transaction3);
			transactionRepository.save(transaction3);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT , 250.00 , "Groseries" , LocalDateTime.now().minusDays(2));
			account1.addTransaction(transaction4);
			transactionRepository.save(transaction4);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT , 600.00 , "Cigarettes" , LocalDateTime.now().plusDays(1));
			account1.addTransaction(transaction5);
			transactionRepository.save(transaction5);
			Transaction transaction6 = new Transaction(TransactionType.CREDIT , 450.10 , "Drinks" , LocalDateTime.now().minusDays(1));
			account3.addTransaction(transaction6);
			transactionRepository.save(transaction6);
			Transaction transaction7 = new Transaction(TransactionType.DEBIT , 1250.00 , "Home" , LocalDateTime.now().minusDays(5));
			account3.addTransaction(transaction7);
			transactionRepository.save(transaction7);

			Loan loan1 = new Loan("Mortgage" , 500000.00 , List.of(12, 24, 36, 48, 60));
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal" , 100000.00 , List.of(6, 12, 24));
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automotive" , 300000.00 , List.of(6, 12, 24, 36));
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan("Mortgage" , 400000.00 , 60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan("Personal" , 50000.00 , 12);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan("Personal" , 100000.00 , 24);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan("Automotive" , 200000.00 , 36);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card("Melba Morel" , CardType.DEBIT , CardColor.GOLD , "3752-250145-45632" , 456 , LocalDate.now() , LocalDate.now().plusYears(5));
			client1.addCard(card1);
			cardRepository.save(card1);
			Card card2 = new Card("Melba Morel" , CardType.CREDIT , CardColor.TITANIUM , "3752-8771-4575-6392" , 788 , LocalDate.now() , LocalDate.now().plusYears(5));
			client1.addCard(card2);
			cardRepository.save(card2);
			Card card3 = new Card("Santiago Aristizabal" , CardType.CREDIT , CardColor.SILVER , "3777-8561-8585-2291" , 421 , LocalDate.now() , LocalDate.now().plusYears(5));
			client2.addCard(card3);
			cardRepository.save(card3);

		};
	}
}
