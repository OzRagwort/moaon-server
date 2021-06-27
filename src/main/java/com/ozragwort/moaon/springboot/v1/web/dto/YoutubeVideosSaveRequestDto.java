package com.ozragwort.moaon.springboot.v1.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeVideosSaveRequestDto {

    private String videoId;

    private String secret;

    @Builder
    public YoutubeVideosSaveRequestDto(String videoId, String secret) {
        this.videoId = videoId;
        this.secret = secret;
    }

}
