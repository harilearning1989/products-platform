package com.demo.utils.date;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private DateUtils() {}

    public static String format(LocalDateTime dateTime) {
        return dateTime.format(ISO_FORMAT);
    }

    public static LocalDateTime parse(String date) {
        return LocalDateTime.parse(date, ISO_FORMAT);
    }

    public static long toEpoch(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.UTC);
    }
}
