package com.mindhub.homebanking.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public  class TransactionUtils {

    public static Date stringtoDate (String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(string); }

    public static LocalDateTime dateToLocalDateTime (Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();     }
}
