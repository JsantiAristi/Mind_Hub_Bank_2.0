package com.mindhub.homebanking.services;

import com.mindhub.homebanking.Models.Loan;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;

import java.util.List;

public interface LoanService {
    void saveLoan(Loan loan);
    Loan loanById(LoanApplicationDTO loanApplicationDTO);
    List<LoanDTO> getLoanDTO();
}
