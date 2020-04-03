package com.pertamina.portal.core.utils;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringUtils {

    public static String formatDateMonth(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMMM yyyy");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static String formatDateFull(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static String formatDateSimple(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static String formatDateSimpleMM(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("d/MM/yyyy");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static Date toDate(String createdOn) {
        // 2019-08-21T09:16:45.75
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SS");

        try {
            return df.parse(createdOn);
        } catch (ParseException e) {
            //e.printStackTrace();

            df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");

            try {
                return df.parse(createdOn);
            } catch (ParseException ex) {

                try {
                    df = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
                    return df.parse(createdOn);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                return null;
            }
        }
    }

    public static Date toDateYyyyMmDd(String strDate) {
        // 20191231
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String reformatDateYyyyMmDd(String strDate) {
        Date date = toDateYyyyMmDd(strDate);
        return formatDateFull(date);
    }

    public static String formatDateNoSpace(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String strDate = dateFormat.format(date);

        return strDate;
    }

    public static String formatCurrency(String strNumber) throws Exception {
        if (strNumber == null && strNumber.length() == 0) {
            throw new Exception("input strNumber must be not null or empty");
        }

        BigDecimal bd;

        try {
            bd = new BigDecimal(strNumber);
        } catch (Exception e) {
            throw e;
        }

        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');

        DecimalFormat formatter = new DecimalFormat("###,###.##", symbols);
        return formatter.format(bd.longValue());
    }
}
