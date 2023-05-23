package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Loan;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    void saveLoan(Loan loan);
    Loan loanById(long id);
    List<LoanDTO> getLoanDTO();
    List<Loan> getLoan();
}
