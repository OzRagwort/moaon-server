package com.ozragwort.moaon.springboot.v1.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChannelsSaveRequestDto {

    private Long categoryId;

    private String channelId;

    private String channelName;

    private String channelThumbnail;

    private String uploadsList;

    private int subscribers;

    private String bannerExternalUrl;

    @Builder
    public ChannelsSaveRequestDto(Long categoryId,
                                  String channelId,
                                  String channelName,
                                  String channelThumbnail,
                                  String uploadsList,
                                  int subscribers,
                                  String bannerExternalUrl) {
        this.categoryId = categoryId;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
        this.bannerExternalUrl = bannerExternalUrl;
    }

}
