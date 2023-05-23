package com.mindhub.homebanking.Utils;

import java.util.Random;

public final class AccountUtil {

    public AccountUtil() {
    }

    public static String aleatoryNumber() {
        Random random = new Random();
        String aleatoryVin = null;
        int numberRandom = 0;
        int min = 100000;
        int max = 899999;
        numberRandom = random.nextInt(max + min);
        aleatoryVin = "VIN-" + String.format("%08d", numberRandom);
        return aleatoryVin;
    }
}
