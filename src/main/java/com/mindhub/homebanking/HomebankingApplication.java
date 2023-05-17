package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.*;
import com.mindhub.homebanking.Utils.CardUtils;
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
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//	@Autowired
//	private AccountService accountService;
//	@Autowired
//	private CardService cardService;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner initData(ClientRespository clientRepository , AccountRepository accountRepository , TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
//		return (args) -> {
//			Client client1 = new Client("Melba", "Morel" , "melba@mindhub.com" , "../../assets/chico.png" , passwordEncoder.encode("melba"));
//			clientRepository.save(client1);
//			Client client2 = new Client("Santiago" , "Aristizabal" , "jsanti@gmail.com" , "../../assets/chico.png" , passwordEncoder.encode("santi123"));
//			clientRepository.save(client2);
//
//			Account account1 = new Account(AccountType.SAVING,"VIN-001", LocalDateTime.now(), 500000.00, true);
//			client1.addAccount(account1);
//			accountRepository.save(account1);
//			Account account2 = new Account(AccountType.CURRENT,"VIN-002", LocalDateTime.now(), 7500.00, true);
//			client1.addAccount(account2);
//			accountRepository.save(account2);
//			Account account3 = new Account(AccountType.SAVING, accountService.aleatoryNumber(), LocalDateTime.now(), 120000.50, true);
//			client2.addAccount(account3);
//			accountRepository.save(account3);
//
//			Transaction transaction1 = new Transaction(TransactionType.DEBIT , 1000.00 , "Videogames" , LocalDateTime.now(), true, account1.getBalance());
//			account1.addTransaction(transaction1);
//			transactionRepository.save(transaction1);
//			Transaction transaction2 = new Transaction(TransactionType.CREDIT , 2000.00 , "Alcohol" , LocalDateTime.now().plusDays(2), true, account2.getBalance());
//			account2.addTransaction(transaction2);
//			transactionRepository.save(transaction2);
//
//			Loan loan1 = new Loan("Mortgage" , 500000.00 , List.of(12, 24, 36, 48, 60) , "Allows you to buy a property without having to pay the full purchase price upfront.", 1.2);
//			loanRepository.save(loan1);
//			Loan loan2 = new Loan("Personal" , 100000.00 , List.of(6, 12, 24) , "Can be used for a wide range of purposes, giving you flexibility and control over your finances.", 1.3);
//			loanRepository.save(loan2);
//			Loan loan3 = new Loan("Automotive" , 300000.00 , List.of(6, 12, 24, 36), "Allows you to purchase a car that you might not be able to afford to pay for upfront.", 1.4);
//			loanRepository.save(loan3);
//
//			ClientLoan clientLoan1 = new ClientLoan( 400000.00,	400000.00*loan1.getInterest()	, 60);
//			client1.addClientLoan(clientLoan1);
//			loan1.addClientLoan(clientLoan1);
//			clientLoanRepository.save(clientLoan1);
//			ClientLoan clientLoan2 = new ClientLoan( 50000.00,	50000.00*loan2.getInterest() , 12);
//			client1.addClientLoan(clientLoan2);
//			loan2.addClientLoan(clientLoan2);
//			clientLoanRepository.save(clientLoan2);
//			ClientLoan clientLoan3 = new ClientLoan(100000.00,	100000.00*loan3.getInterest() , 24);
//			client2.addClientLoan(clientLoan3);
//			loan2.addClientLoan(clientLoan3);
//			clientLoanRepository.save(clientLoan3);
//			ClientLoan clientLoan4 = new ClientLoan( 200000.00,	200000.00+(200000.00*0.2) , 36);
//			client2.addClientLoan(clientLoan4);
//			loan3.addClientLoan(clientLoan4);
//			clientLoanRepository.save(clientLoan4);
//
//			Card card1 = new Card(CardType.DEBIT , CardColor.GOLD , cardService.aleatoryNumberCardsNotRepeat(), CardUtils.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5), true);
//			client1.addCard(card1);
//			cardRepository.save(card1);
//			Card card2 = new Card(CardType.CREDIT , CardColor.TITANIUM , cardService.aleatoryNumberCardsNotRepeat(), CardUtils.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5), true);
//			client1.addCard(card2);
//			cardRepository.save(card2);
//			Card card3 = new Card(CardType.CREDIT , CardColor.SILVER , cardService.aleatoryNumberCardsNotRepeat(), CardUtils.aleatoryNumberCvv() , LocalDate.now() , LocalDate.now().plusYears(5), true);
//			client2.addCard(card3);
//			cardRepository.save(card3);
//			Card card4 = new Card(CardType.CREDIT , CardColor.TITANIUM , cardService.aleatoryNumberCardsNotRepeat(), CardUtils.aleatoryNumberCvv() , LocalDate.now().minusYears(5) , LocalDate.now().minusDays(1), true);
//			client2.addCard(card4);
//			cardRepository.save(card4);
//		};
//	}
}
