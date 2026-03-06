package com.product.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class DateFormatterUtil {

    private DateFormatterUtil() {}

    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ISO_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";

    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    /* =========================
       Date → String
       ========================= */

    public static String dateToString(Date date, String pattern) {
        if (date == null) return null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return Instant.ofEpochMilli(date.getTime())
                .atZone(DEFAULT_ZONE)
                .format(formatter);
    }

    public static String dateToString(Date date) {
        return dateToString(date, DEFAULT_DATE_TIME_PATTERN);
    }

    public static String localDateToString(LocalDate date, String pattern) {
        if (date == null) return null;
        return date.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static String localDateTimeToString(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) return null;
        return dateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /* =========================
       String → Date
       ========================= */

    public static Date stringToDate(String dateStr, String pattern) {
        if (dateStr == null) return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);

        return Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    public static Date stringToDate(String dateStr) {
        return stringToDate(dateStr, DEFAULT_DATE_TIME_PATTERN);
    }

    public static LocalDate stringToLocalDate(String dateStr, String pattern) {
        if (dateStr == null) return null;
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime stringToLocalDateTime(String dateStr, String pattern) {
        if (dateStr == null) return null;
        return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
    }

    /* =========================
       LocalDateTime ↔ Date
       ========================= */

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        return Date.from(localDateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (date == null) return null;
        return Instant.ofEpochMilli(date.getTime())
                .atZone(DEFAULT_ZONE)
                .toLocalDateTime();
    }

    /* =========================
       LocalDate ↔ Date
       ========================= */

    public static Date localDateToDate(LocalDate localDate) {
        if (localDate == null) return null;
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE).toInstant());
    }

    public static LocalDate dateToLocalDate(Date date) {
        if (date == null) return null;
        return Instant.ofEpochMilli(date.getTime())
                .atZone(DEFAULT_ZONE)
                .toLocalDate();
    }

    /* =========================
       Epoch Conversions
       ========================= */

    public static long dateToEpochMillis(Date date) {
        if (date == null) return 0;
        return date.getTime();
    }

    public static Date epochMillisToDate(long millis) {
        return new Date(millis);
    }

    /* =========================
       Instant Conversions
       ========================= */

    public static Instant dateToInstant(Date date) {
        if (date == null) return null;
        return date.toInstant();
    }

    public static Date instantToDate(Instant instant) {
        if (instant == null) return null;
        return Date.from(instant);
    }

    /* =========================
       Validation
       ========================= */

    public static boolean isValidDate(String dateStr, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate.parse(dateStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

}