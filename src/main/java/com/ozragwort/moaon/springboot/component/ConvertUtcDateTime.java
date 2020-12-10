package com.ozragwort.moaon.springboot.component;

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

}
