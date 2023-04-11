package com.mindhub.homebanking;

import com.mindhub.homebanking.Models.Account;
import com.mindhub.homebanking.Models.Client;
import com.mindhub.homebanking.Models.Transaction;
import com.mindhub.homebanking.Models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRespository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRespository clientRepository , AccountRepository accountRepository , TransactionRepository transactionRepository) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "Morel" , "melba@mindhub.com");
			clientRepository.save(client1);
			Client client2 = new Client("Santiago" , "Aristizabal" , "jsanti@gmail.com");
			clientRepository.save(client2);

			Account account1 = new Account("VIN001" , LocalDateTime.now() , 5000.00);
			client1.addAccount(account1);
			accountRepository.save(account1);
			Account account2 = new Account("VIN002" , LocalDateTime.now().plusDays(1) , 7500.00);
			client1.addAccount(account2);
			accountRepository.save(account2);
			Account account3 = new Account("VIN003" , LocalDateTime.now().plusDays(2) , 6000.50);
			client2.addAccount(account3);
			accountRepository.save(account3);

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
		};
	}
}
