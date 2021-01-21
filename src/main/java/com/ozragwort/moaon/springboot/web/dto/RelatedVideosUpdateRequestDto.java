package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RelatedVideosUpdateRequestDto {

    private List<String> relatedVideo;

    @Builder
    public RelatedVideosUpdateRequestDto(List<String> relatedVideo) {
        this.relatedVideo = relatedVideo;
    }

}
