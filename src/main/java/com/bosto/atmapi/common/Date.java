package com.bosto.atmapi.common;

import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;


@Slf4j
public class Date {
    public static SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    public  static String now() {
        Calendar cal = Calendar.getInstance();
        return sdf.format(cal.getTime());
    }

    public  static String tomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return sdf.format(cal.getTime());
    }



    public  static String date2String(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    public static String laterOfMonth(int month) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        return sdf.format(cal.getTime());
    }

    public static String laterOfMonth(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
        } catch (Exception e) {
            log.error("Parse time fail");
        }
        calendar.add(Calendar.MONTH, month);
        return sdf.format(calendar.getTime());
    }

    public static boolean laterThanNow(String date) {
        Calendar cal = Calendar.getInstance();
        return cal.getTime().before(java.util.Date.from(Instant.parse(date+"T01:00:00.00Z")));
    }

    public static boolean laterThanOrderDate(String date, String orderDate) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(orderDate));
        } catch (Exception e) {
            log.error("Parse time fail");
        }
        return cal.getTime().before(java.util.Date.from(Instant.parse(date+"T01:00:00.00Z")));
    }

    public static boolean leftBeforeRight(String date1, String date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse(date1));
        } catch (Exception e) {
            log.error("Parse time fail");
        }
        return cal.getTime().before(java.util.Date.from(Instant.parse(date2+"T01:00:00.00Z")));
    }


}
