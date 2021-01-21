package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosTagsPopularitySaveRequestDto {

    private Long categoryId;
    private String tags;

    @Builder
    public VideosTagsPopularitySaveRequestDto(Long categoryId, String tags) {
        this.categoryId = categoryId;
        this.tags = tags;
    }

    public VideosTagsPopularity toEntity() {
        return VideosTagsPopularity.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();
    }

}
