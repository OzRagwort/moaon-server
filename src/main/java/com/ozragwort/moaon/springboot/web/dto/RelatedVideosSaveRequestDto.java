package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRelations;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RelatedVideosSaveRequestDto {

    private String videoId;

    private Videos relatedVideos;

    @Builder
    public RelatedVideosSaveRequestDto(String videoId, Videos relatedVideos) {
        this.videoId = videoId;
        this.relatedVideos = relatedVideos;
    }

    public VideosRelations toEntity() {
        return VideosRelations.builder()
                .videoId(this.videoId)
                .relatedVideos(this.relatedVideos)
                .build();
    }

}
