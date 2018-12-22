package com.zugara.atproj.lampsplus.utils;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by andre on 22-Dec-18.
 */

public class DateUtil {

    public static String getCurrentDate() {
        Calendar date = Calendar.getInstance();

        DecimalFormat dblDecFormat = new DecimalFormat("00");

        String result = date.get(Calendar.YEAR) + "-" +
                dblDecFormat.format(date.get(Calendar.MONTH)+1) + "-" +
                dblDecFormat.format(date.get(Calendar.DAY_OF_MONTH));

        return result;
    }

    public static String getCurrentDateTime() {
        Calendar date = Calendar.getInstance();

        DecimalFormat dblDecFormat = new DecimalFormat("00");

        String result = getCurrentDate()
                + " " + dblDecFormat.format(date.get(Calendar.HOUR))
                + ":" + dblDecFormat.format(date.get(Calendar.MINUTE))
                + ":" + dblDecFormat.format(date.get(Calendar.SECOND));

        return result;
    }
}
