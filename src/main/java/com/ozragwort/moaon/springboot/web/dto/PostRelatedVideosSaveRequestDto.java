package com.ozragwort.moaon.springboot.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostRelatedVideosSaveRequestDto {

    private String videoId;

    private List<String> relatedVideo;

}
