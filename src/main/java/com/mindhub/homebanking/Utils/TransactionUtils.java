package com.mindhub.homebanking.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public  class TransactionUtils {

    public static Date stringtoDate (String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(string);
    }
    public static LocalDateTime dateToLocalDateTime (Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public static Date stringtoDateFinal (String string) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(string);
    }

    public static String getStringDateFromLocalDateTime(LocalDateTime date){
        int year = date.getYear();
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        return year + "-" + month + "-" + day;}

    public static String getStringHourFromLocalDateTime(LocalDateTime date){
        int hour = date.getHour();
        int min = date.getMinute();

        return String.valueOf(hour<10?'0':"") + hour + ":" + (min<10?'0':"") + min;}
}
