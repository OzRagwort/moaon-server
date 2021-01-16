package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.videos.VideosRelations;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelatedVideosResponseDto {

    private Long idx;

    private String videoId;

    private Videos relatedVideos;

    @Builder
    public RelatedVideosResponseDto(VideosRelations relatedVideos) {
        this.idx = relatedVideos.getIdx();
        this.videoId = relatedVideos.getVideoId();
        this.relatedVideos = relatedVideos.getRelatedVideos();
    }

}
