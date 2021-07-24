package com.ozragwort.moaon.springboot.dto.channels;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class ChannelsSaveRequestDto {

    @NotNull(message = "categoryId must be provided")
    private Long categoryId;

    @NotNull(message = "channelId must be provided")
    private String channelId;

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

    public Channels toEntity(Categories categories) {
        return Channels.builder()
                .categories(categories)
                .channelId(channelId)
                .channelName(channelName)
                .channelThumbnail(channelThumbnail)
                .uploadsList(uploadsList)
                .subscribers(subscribers)
                .bannerExternalUrl(bannerExternalUrl)
                .build();
    }
}
