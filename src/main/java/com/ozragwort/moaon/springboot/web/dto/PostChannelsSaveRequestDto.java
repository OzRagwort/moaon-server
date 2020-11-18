package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostChannelsSaveRequestDto {

    private Long categoryId;

    private String channelId;

    @Builder
    public PostChannelsSaveRequestDto(Long categoryId, String channelId) {
        this.categoryId = categoryId;
        this.channelId = channelId;
    }

}
