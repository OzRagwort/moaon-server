package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosTagsPopularityUpdateRequestDto {

    private Long categoryId;
    private String tags;

    @Builder
    public VideosTagsPopularityUpdateRequestDto(Long categoryId, String tags) {
        this.categoryId = categoryId;
        this.tags = tags;
    }

}
