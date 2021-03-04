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
        double vw = 0.25, lw = 1.05, ldw = 0.5, mw = 0.5;

        if ((viewCount != 0) && (likeCount != 0) && (dislikeCount != 0) && (commentCount != 0)) {
            return Math.pow(((Math.pow(viewCount, vw) * Math.pow(likeCount, lw)) / (double) dislikeCount) * Math.pow(((double) likeCount / (double) viewCount) * 100, ldw), mw);
        } else {
            double v = viewCount + 1;
            double l = likeCount + 1;
            double d = dislikeCount + 1;
            double c = commentCount + 1;
            return Math.pow(((Math.pow(v, vw) * Math.pow(l, lw)) / d) * Math.pow((l / v) * 100, ldw), mw);
        }
    }

}
