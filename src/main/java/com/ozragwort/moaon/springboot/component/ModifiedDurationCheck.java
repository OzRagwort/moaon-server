package com.ozragwort.moaon.springboot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class ModifiedDurationCheck {

    public boolean ModifiedDurationTimeCheck(LocalDateTime modifiedDate) {
        LocalDateTime now = LocalDateTime.now();
        Period period = Period.between(modifiedDate.toLocalDate(), now.toLocalDate());
        long minutes = ChronoUnit.MINUTES.between(modifiedDate.toLocalTime(), now.toLocalTime());

        return period.isZero() && minutes < 60;
    }

}
