package com.ozragwort.moaon.springboot.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConvertTo {

    public static LocalDateTime StringToUTCDateTime(String time) {
        return LocalDateTime.from(
                Instant.from(
                        DateTimeFormatter.ISO_DATE_TIME.parse(time)
                ).atZone(ZoneId.of("UTC"))
        );
    }

    public static long DurationStringToSecond(String duration) {
        return Duration.parse(duration).getSeconds();
    }

    public static String DurationSecondToString(long second) {
        return Duration.ofSeconds(second).toString();
    }


}
