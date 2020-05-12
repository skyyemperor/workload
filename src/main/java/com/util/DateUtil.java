package com.util;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * LocalDate：只有日期，诸如：2019-07-13
 * LocalTime：只有时间，诸如：08:30
 * LocalDateTime：日期+时间，诸如：2019-07-13 08:30
 */
public class DateUtil {
    public static LocalDate date2LocalDate(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime date2LocalDateTime(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalTime date2LocalTime(Date date) {
        if (null == date) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }


    public static Date convert2Date(LocalDate localDate) {
        if (null == localDate) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date convert2Date(LocalDateTime localDateTime) {
        if (null == localDateTime) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat dateFormat2 = new SimpleDateFormat(pattern);
        return dateFormat2.format(date);
    }

    public static String formatDate(LocalDateTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDate(LocalDate date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String formatDate(LocalTime date, String pattern) {
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }


}
