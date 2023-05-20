package com.mindhub.homebanking.Utils;

import java.util.Random;

public final class AccountUtil {

    public AccountUtil() {
    }

    public static String aleatoryNumber() {
        Random random = new Random();
        int aleatory;
        String aleatoryVin = null;
        int min = 100000;
        int max = 899999;
        aleatory = random.nextInt(max + min);
        if( aleatory >= 100000 && aleatory <= 999999 ){
            aleatoryVin = "VIN-" + random.nextInt(max + min);
        } else {
            aleatoryVin = "VIN-" + random.nextInt(max + min) + "0";
        }
        return aleatoryVin;
    }
}
