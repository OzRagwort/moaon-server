package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.videos.VideosQuality;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosQualityResponseDto {

    private VideosResponseDto videos;
    private String channelId;
    private double score;

    public VideosQualityResponseDto(VideosQuality videosQuality) {
        this.videos = new VideosResponseDto(videosQuality.getVideos());
        this.channelId = videosQuality.getChannelId();
        this.score = videosQuality.getScore();
    }

}
