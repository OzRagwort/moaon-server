package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChannelsSaveRequestDto {

    private Categories categories;

    private String channelId;

    private String channelName;

    private String channelThumbnail;

    private String uploadsList;

    private int subscribers;

    @Builder
    public ChannelsSaveRequestDto(Categories categories,
                                  String channelId,
                                  String channelName,
                                  String channelThumbnail,
                                  String uploadsList,
                                  int subscribers) {
        this.categories = categories;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelThumbnail = channelThumbnail;
        this.uploadsList = uploadsList;
        this.subscribers = subscribers;
    }

    public Channels toEntity() {
        return Channels.builder()
                .categories(categories)
                .channelId(channelId)
                .channelName(channelName)
                .channelThumbnail(channelThumbnail)
                .uploadsList(uploadsList)
                .subscribers(subscribers)
                .build();
    }

}
