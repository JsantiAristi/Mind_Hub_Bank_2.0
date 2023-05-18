package com.mindhub.homebanking.Utils;

import java.util.Random;

public final class AccountUtil {

    public AccountUtil() {
    }

    public static String aleatoryNumber() {
        Random random = new Random();
        int min = 100000;
        int max = 899999;
        return ("VIN-" + random.nextInt(max + min));
    }
}
