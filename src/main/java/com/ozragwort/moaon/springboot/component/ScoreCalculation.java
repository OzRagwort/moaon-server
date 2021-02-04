package com.ozragwort.moaon.springboot.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.lang.Math;

@RequiredArgsConstructor
@Component
public class ScoreCalculation {

    public double makeScore(int viewCount,
                            int likeCount,
                            int dislikeCount,
                            int commentCount) {
        if ((viewCount != 0) && (likeCount != 0) && (dislikeCount != 0) && (commentCount != 0)) {
            return (Math.pow(likeCount, 1.1) * (double) likeCount) / ((double) dislikeCount * (double) viewCount);
        } else {
            return 0;
        }
    }

}
