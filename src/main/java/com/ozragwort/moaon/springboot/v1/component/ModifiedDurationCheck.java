package com.ozragwort.moaon.springboot.v1.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class ModifiedDurationCheck {

    private int checkModifiedDateMinutes = 360;
    private int checkPublishedDateMinutes = 10;

    public boolean updateTimeCheck(LocalDateTime publishedDate, LocalDateTime modifiedDate) {
        LocalDateTime now = LocalDateTime.now();
        Period periodModifiedDate = Period.between(modifiedDate.toLocalDate(), now.toLocalDate());
        long minutesModifiedDate = ChronoUnit.MINUTES.between(modifiedDate.toLocalTime(), now.toLocalTime());

        Period periodPublishedDate = Period.between(publishedDate.toLocalDate(), now.toLocalDate());
        long minutesPublishedDate = ChronoUnit.MINUTES.between(publishedDate.toLocalTime(), now.toLocalTime());

        // 영상 업로드 3일 이내인 경우
        if (periodPublishedDate.getDays() < 3) {
            // 최근 수정일이 10분 이내면 갱신
            return periodModifiedDate.isZero() && minutesModifiedDate < checkPublishedDateMinutes;
        } else {
            // 최근 수정일이 360분 이내면 갱신
            return periodModifiedDate.isZero() && minutesModifiedDate < checkModifiedDateMinutes;
        }
    }

}
