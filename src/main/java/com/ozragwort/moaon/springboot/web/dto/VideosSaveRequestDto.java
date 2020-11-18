package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.util.List;

@Getter
@NoArgsConstructor
public class VideosSaveRequestDto {

    private Channels channels;

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

    @Builder
    public VideosSaveRequestDto(Channels channels,
                                String videoId,
                                String videoName,
                                String videoThumbnail,
                                String videoDescription,
                                String videoPublishedDate,
                                String videoDuration,
                                boolean videoPublicStatsViewable,
                                int viewCount,
                                int likeCount,
                                int dislikeCount,
                                int commentCount,
                                List<String> tags) {
        this.channels = channels;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.videoPublicStatsViewable = videoPublicStatsViewable;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.tags = tags;
    }

    public Videos toEntity() {
        return Videos.builder()
                .channels(channels)
                .videoId(videoId)
                .videoName(videoName)
                .videoThumbnail(videoThumbnail)
                .videoDescription(videoDescription)
                .videoPublishedDate(videoPublishedDate)
                .videoDuration(videoDuration)
                .videoPublicStatsViewable(videoPublicStatsViewable)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .tags(tags)
                .build();
    }

}
