package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelatedVideosUpdateRequestDto {

    private String videoId;

    private String relatedVideo;

    @Builder
    public RelatedVideosUpdateRequestDto(String videoId, String relatedVideo) {
        this.videoId = videoId;
        this.relatedVideo = relatedVideo;
    }

}
