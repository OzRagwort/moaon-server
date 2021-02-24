package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeChannelUploadsListRequestDto {

    private String channelId;

    private String secret;

    @Builder
    public YoutubeChannelUploadsListRequestDto(String channelId, String secret) {
        this.channelId = channelId;
        this.secret = secret;
    }

}
