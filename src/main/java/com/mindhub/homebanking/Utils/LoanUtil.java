package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.Models.Loan;

public final class LoanUtil {

    private LoanUtil() {
    }

    public static double calculateLoan(Loan loan){
        double interest;
        if (loan.getName().equalsIgnoreCase("Mortgage")){
            return interest = 1.2;
        } else if ( loan.getName().equalsIgnoreCase("Personal") ) {
            return interest = 1.3;
        } else if ( loan.getName().equalsIgnoreCase("Automotive") ) {
            return interest = 1.4;
        } else {
            return interest = 1.2;
        }
    }

}
