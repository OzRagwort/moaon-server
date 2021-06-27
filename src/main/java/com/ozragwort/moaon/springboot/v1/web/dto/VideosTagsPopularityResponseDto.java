package com.ozragwort.moaon.springboot.v1.web.dto;

import com.ozragwort.moaon.springboot.v1.domain.videos.VideosTagsPopularity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosTagsPopularityResponseDto {

    private Long idx;
    private Long categoryId;
    private String tags;

    public VideosTagsPopularityResponseDto(VideosTagsPopularity videosTagsPopularity) {
        this.idx = videosTagsPopularity.getIdx();
        this.categoryId = videosTagsPopularity.getCategoryId();
        this.tags = videosTagsPopularity.getTags();
    }

}
