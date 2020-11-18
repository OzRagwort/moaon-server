package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostChannelUploadsListDto {

    private String channelId;

    @Builder
    public PostChannelUploadsListDto(String channelId) {
        this.channelId = channelId;
    }

}
