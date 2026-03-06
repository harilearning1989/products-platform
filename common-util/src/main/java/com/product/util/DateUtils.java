package com.product.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.of("UTC"));

    private DateUtils() {}

    public static String formatUtc(Instant instant) {
        return instant == null ? null : FORMATTER.format(instant);
    }
}
