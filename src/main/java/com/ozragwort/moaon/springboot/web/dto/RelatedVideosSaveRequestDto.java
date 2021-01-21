package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RelatedVideosSaveRequestDto {

    private List<String> relatedVideo;

    @Builder
    public RelatedVideosSaveRequestDto(List<String> relatedVideo) {
        this.relatedVideo = relatedVideo;
    }

}
