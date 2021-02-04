package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosQualitySaveRequestDto {

    private String videoId;
    private String channelId;
    private double score;

    @Builder
    public VideosQualitySaveRequestDto(String videoId,
                                       String channelId,
                                       double score) {
        this.videoId = videoId;
        this.channelId = channelId;
        this.score = score;
    }

}
