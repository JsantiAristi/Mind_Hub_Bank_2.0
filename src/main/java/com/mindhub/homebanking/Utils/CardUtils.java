package com.mindhub.homebanking.Utils;

import com.mindhub.homebanking.repositories.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;

public final class CardUtils {

    private CardUtils() {
    }

    public static String getCards() {
        String numberCard = "";
        String numberCardInit = "";
        for (int i = 0; i < 4; i++) {
            int min = 1000;
            int max = 8999;
            numberCardInit += (int) (Math.random() * max + min) + "-";
        }
        numberCard = numberCardInit.substring(0 , numberCardInit.length() - 1);
        return numberCard;
    }

    public static int aleatoryNumberCvv() {
        int min = 100;
        int max = 899;
        return (int) (Math.random() * max + min);
    }
}
