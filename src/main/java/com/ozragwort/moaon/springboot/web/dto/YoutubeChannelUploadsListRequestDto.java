package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class YoutubeChannelUploadsListRequestDto {

    private String channelId;

    @Builder
    public YoutubeChannelUploadsListRequestDto(String channelId) {
        this.channelId = channelId;
    }

}
