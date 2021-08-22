package com.ozragwort.moaon.springboot.dto.channels;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class ChannelsUpdateRequestDto {

    @NotNull(message = "channelName must be provided")
    private String channelName;

    @NotNull(message = "channelThumbnail must be provided")
    private String channelThumbnail;

    @NotNull(message = "uploadsList must be provided")
    private String uploadsList;

    @NotNull(message = "subscribers must be provided")
    @PositiveOrZero(message = "subscribers must be positive or zero")
    private int subscribers;

    private String bannerExternalUrl;

    public ChannelsUpdateRequestDto(String channelName,
                                    String channelThumbnail,
                                    String uploadsList,
                                    int subscribers,
                                    String bannerExternalUrl) {
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
        this.bannerExternalUrl = bannerExternalUrl;
    }
}
