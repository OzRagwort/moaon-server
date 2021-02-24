package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeChannelsSaveRequestDto {

    private Long categoryId;

    private String channelId;

    private String secret;

    @Builder
    public YoutubeChannelsSaveRequestDto(Long categoryId, String channelId, String secret) {
        this.categoryId = categoryId;
        this.channelId = channelId;
        this.secret = secret;
    }

}
