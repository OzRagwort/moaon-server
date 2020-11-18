package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;

@Getter
@NoArgsConstructor
public class VideosResponseDto {

    private Long idx;

    private Long categoriesIdx;

    private String channelId;

    private String videoId;

    private String videoName;

    private String videoThumbnail;

    private String videoDescription;

    private String videoPublishedDate;

    private String videoDuration;

    private boolean videoPublicStatsViewable;

    private int viewCount;

    private int likeCount;

    private int dislikeCount;

    private int commentCount;

    private List<String> tags;

    public VideosResponseDto(Videos videos) {
        this.idx = videos.getIdx();
        this.categoriesIdx = videos.getChannels().getCategories().getIdx();
        this.channelId = videos.getChannels().getChannelId();
        this.videoId = videos.getVideoId();
        this.videoName = videos.getVideoName();
        this.videoThumbnail = videos.getVideoThumbnail();
        this.videoDescription = videos.getVideoDescription();
        this.videoPublishedDate = videos.getVideoPublishedDate();
        this.videoDuration = videos.getVideoDuration();
        this.videoPublicStatsViewable = videos.isVideoPublicStatsViewable();
        this.viewCount = videos.getViewCount();
        this.likeCount = videos.getLikeCount();
        this.dislikeCount = videos.getDislikeCount();
        this.commentCount = videos.getCommentCount();
        this.tags = videos.getTags();
    }

}
