package com.mindhub.homebanking.Utils;

import java.util.Random;

public final class AccountUtil {

    public AccountUtil() {
    }

    public static String aleatoryNumber() {
        Random random = new Random();
        String aleatoryVin = null;
        int min = 100000;
        int max = 899999;
        aleatoryVin = "VIN-" + random.nextInt(max + min);
        return aleatoryVin;
    }
}
