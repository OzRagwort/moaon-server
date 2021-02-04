package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class VideosResponseDto {

    private Long idx;

    private Long categoriesIdx;

    private Channels channels;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private String videoId;

    private String videoName;

    private String videoThumbnail;

    private String videoDescription;

    private LocalDateTime videoPublishedDate;

    private String videoDuration;

    private boolean videoEmbeddable;

    private int viewCount;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private double score;

    private List<String> tags;

    public VideosResponseDto(Videos videos) {
        this.idx = videos.getIdx();
        this.categoriesIdx = videos.getChannels().getCategories().getIdx();
        this.channels = videos.getChannels();
        this.createdDate = videos.getCreatedDate();
        this.modifiedDate = videos.getModifiedDate();
        this.videoId = videos.getVideoId();
        this.videoName = videos.getVideoName();
        this.videoThumbnail = videos.getVideoThumbnail();
        this.videoDescription = videos.getVideoDescription();
        this.videoPublishedDate = videos.getVideoPublishedDate();
        this.videoDuration = videos.getVideoDuration();
        this.videoEmbeddable = videos.isVideoEmbeddable();
        this.viewCount = videos.getViewCount();
        this.likeCount = videos.getLikeCount();
        this.dislikeCount = videos.getDislikeCount();
        this.commentCount = videos.getCommentCount();
        this.score = videos.getScore();
        this.tags = videos.getTags().stream().map(String::new).collect(Collectors.toList());
    }

}
