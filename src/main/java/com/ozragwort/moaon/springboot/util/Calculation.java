package com.ozragwort.moaon.springboot.util;

public class Calculation {

    public static double calcScore(int viewCount,
                                   int likeCount,
                                   int dislikeCount,
                                   int commentCount) {
        if (dislikeCount == 0) {
            dislikeCount++;
        }
        return (double) likeCount / (double) dislikeCount;
    }

}
