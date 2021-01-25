package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeVideosSaveRequestDto {

    private String videoId;

    @Builder
    public YoutubeVideosSaveRequestDto(String videoId) {
        this.videoId = videoId;
    }

}
