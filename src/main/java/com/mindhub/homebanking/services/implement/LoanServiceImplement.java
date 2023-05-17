package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.Models.Loan;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;


@Service
public class LoanServiceImplement implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Override
    public void saveLoan(Loan loan) {
        loanRepository.save(loan);
    }

    @Override
    public Loan loanById(long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    public List<LoanDTO> getLoanDTO() {
        return loanRepository.findAll().stream().map(loan -> new LoanDTO(loan)).collect(toList());
    }

    @Override
    public List<Loan> getLoan() {
        return loanRepository.findAll();
    }
}
