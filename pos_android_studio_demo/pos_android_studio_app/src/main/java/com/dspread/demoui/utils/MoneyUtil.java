package com.dspread.demoui.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Amount processing class
 */
public class MoneyUtil {

    private static DecimalFormat doubleDF = new DecimalFormat("#0.00");

    public static String formatDouble2Str4Money(double d) {
        return doubleDF.format(d);
    }

    public static String fen2yuan(long fen) {
        BigDecimal bigDecimal = new BigDecimal(fen);
        return formatDouble2Str4Money(fen / 100.00);
    }
    public static String yuan2fen(long yuan) {
        BigDecimal bigDecimal = new BigDecimal(yuan);
        return formatDouble2Str4Money(yuan *100.00);
    }
    public static String fen2yuan1(long fen) {
        BigDecimal bigDecimal = new BigDecimal(fen);
        return formatDouble2Str4Money(fen / 1000.00);
    }
    public static String fen2yuan2(long fen) {
        BigDecimal bigDecimal = new BigDecimal(fen);
        return formatDouble2Str4Money(fen / 1.00);
    }

    public static Double fenTrans2Yuan(Long fen) {
        return Double.parseDouble(fen2yuan(fen));
    }

    /**
     * Convert a unit of a yuan to a minute (x100)
     *
     * @param amount
     * @return
     */
    public static long yuan2fen(double amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).longValue();
    }

}