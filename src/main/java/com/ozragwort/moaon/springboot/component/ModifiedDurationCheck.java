package com.ozragwort.moaon.springboot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;

@RequiredArgsConstructor
@Component
public class ModifiedDurationCheck {

    private LocalDateTime modifiedDate;

    public ModifiedDurationCheck(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int get() {

        LocalDateTime now = LocalDateTime.now();

        Period period = Period.between(modifiedDate.toLocalDate(), now.toLocalDate());

        return period.getDays();
    }

}
