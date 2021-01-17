package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChannelsResponseDto {

    private Long idx;

    private Long categories;

    private String channelId;

    private String channelName;

    private String channelThumbnail;

    private String uploadsList;

    private int subscribers;

    private String bannerExternalUrl;

    public ChannelsResponseDto(Channels channels) {
        this.idx = channels.getIdx();
        this.categories = channels.getCategories().getIdx();
        this.channelId = channels.getChannelId();
        this.channelName = channels.getChannelName();
        this.channelThumbnail = channels.getChannelThumbnail();
        this.uploadsList = channels.getUploadsList();
        this.subscribers = channels.getSubscribers();
        this.bannerExternalUrl = channels.getBannerExternalUrl();
    }

}
