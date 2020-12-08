package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class VideosUpdateRequestDto {

    private String videoName;

    private String videoThumbnail;

    private String videoDescription;

    private String videoPublishedDate;

    private String videoDuration;

    private boolean videoEmbeddable;

    private int viewCount;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private List<String> tags;

    @Builder
    public VideosUpdateRequestDto(String videoName,
                                  String videoThumbnail,
                                  String videoDescription,
                                  String videoPublishedDate,
                                  String videoDuration,
                                  boolean videoEmbeddable,
                                  int viewCount,
                                  int likeCount,
                                  int dislikeCount,
                                  int commentCount,
                                  List<String> tags) {
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.videoEmbeddable = videoEmbeddable;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.tags = tags;
    }
}
