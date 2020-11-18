package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChannelsUpdateRequestDto {

    private String channelName;
    private String channelThumbnail;
    private String uploadsList;
    private int subscribers;

    @Builder
    public ChannelsUpdateRequestDto(String channelName, String channelThumbnail, String uploadsList, int subscribers) {
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
    }

}
