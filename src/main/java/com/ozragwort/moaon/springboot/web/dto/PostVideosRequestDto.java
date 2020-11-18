package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostVideosRequestDto {

    private String videoId;

    @Builder
    public PostVideosRequestDto(String videoId) {
        this.videoId = videoId;
    }

}
