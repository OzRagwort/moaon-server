package com.ozragwort.moaon.springboot.v1.component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConvertUtcDateTime {

    public static LocalDateTime StringToUTCDateTime(String time) {
        return LocalDateTime.from(
                Instant.from(
                        DateTimeFormatter.ISO_DATE_TIME.parse(time)
                ).atZone(ZoneId.of("UTC"))
        );
    }

    public static LocalDateTime nowTimeUnderHour(int hour) {
        return LocalDateTime.now(ZoneId.of("UTC")).plusHours(hour * -1);
    }

}
