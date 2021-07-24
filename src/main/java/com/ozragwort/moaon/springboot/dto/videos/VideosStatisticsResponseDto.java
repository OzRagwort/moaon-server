package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.videos.VideosStatistics;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosStatisticsResponseDto {

    private int viewCount;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private double score;

    public VideosStatisticsResponseDto(VideosStatistics videosStatistics) {
        this.viewCount = videosStatistics.getViewCount();
        this.likeCount = videosStatistics.getLikeCount();
        this.dislikeCount = videosStatistics.getDislikeCount();
        this.commentCount = videosStatistics.getCommentCount();
        this.score = videosStatistics.getScore();
    }

}
