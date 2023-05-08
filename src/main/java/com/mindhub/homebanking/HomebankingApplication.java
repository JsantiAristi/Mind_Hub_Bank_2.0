package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CardService cardService;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRespository clientRepository , AccountRepository accountRepository , TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {
			Client client1 = new Client("Melba", "Morel" , "melba@mindhub.com" , "../../assets/chico.png" , passwordEncoder.encode("melba"));
			clientRepository.save(client1);
			Client client2 = new Client("Santiago" , "Aristizabal" , "jsanti@gmail.com" , "../../assets/chico.png" , passwordEncoder.encode("santi123"));
			clientRepository.save(client2);
			Client client3 = new Client("Carlos" , "Hinestrosa" , "Carlos12@yahoo.com" , "../../assets/chico.png" ,passwordEncoder.encode("carlongas"));
			clientRepository.save(client3);

			Account account1 = new Account("VIN-001", LocalDateTime.now() , 5000.00);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN-002", LocalDateTime.now().plusDays(1) , 7500.00);
			client1.addAccount(account2);
			accountRepository.save(account2);
			Account account3 = new Account(accountService.aleatoryNumber(), LocalDateTime.now().plusDays(2) , 120000.50);
			client2.addAccount(account3);
			accountRepository.save(account3);
			Account account4 = new Account(accountService.aleatoryNumber(), LocalDateTime.now() , 500.50);
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

			Loan loan1 = new Loan("Mortgage" , 500000.00 , List.of(12, 24, 36, 48, 60) , "Allows you to buy a property without having to pay the full purchase price upfront.");
			loanRepository.save(loan1);
			Loan loan2 = new Loan("Personal" , 100000.00 , List.of(6, 12, 24) , "Can be used for a wide range of purposes, giving you flexibility and control over your finances.");
			loanRepository.save(loan2);
			Loan loan3 = new Loan("Automotive" , 300000.00 , List.of(6, 12, 24, 36), "Allows you to purchase a car that you might not be able to afford to pay for upfront.");
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan( 400000.00,	400000.00+(400000.00*0.2)	, 60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepository.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan( 50000.00,	50000.00+(50000.00*0.2) , 12);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepository.save(clientLoan2);
			ClientLoan clientLoan3 = new ClientLoan(100000.00,	100000.00+(100000.00*0.2) , 24);
			client2.addClientLoan(clientLoan3);
			loan2.addClientLoan(clientLoan3);
			clientLoanRepository.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan( 200000.00,	200000.00+(200000.00*0.2) , 36);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(CardType.DEBIT , CardColor.GOLD , cardService.aleatoryNumberCards() , cardService.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5));
			client1.addCard(card1);
			cardRepository.save(card1);
			Card card2 = new Card(CardType.CREDIT , CardColor.TITANIUM , cardService.aleatoryNumberCards() , cardService.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5));
			client1.addCard(card2);
			cardRepository.save(card2);
			Card card3 = new Card(CardType.CREDIT , CardColor.SILVER , cardService.aleatoryNumberCards() , cardService.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5));
			client2.addCard(card3);
			cardRepository.save(card3);
			Card card4 = new Card(CardType.CREDIT , CardColor.TITANIUM , cardService.aleatoryNumberCards() , cardService.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5));
			client2.addCard(card4);
			cardRepository.save(card4);
		};
	}
}
