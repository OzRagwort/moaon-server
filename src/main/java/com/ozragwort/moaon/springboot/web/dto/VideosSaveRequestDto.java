package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class VideosSaveRequestDto {

    private Channels channels;

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

    private List<String> tags;

    @Builder
    public VideosSaveRequestDto(Channels channels,
                                String videoId,
                                String videoName,
                                String videoThumbnail,
                                String videoDescription,
                                LocalDateTime videoPublishedDate,
                                String videoDuration,
                                boolean videoEmbeddable,
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
        this.videoEmbeddable = videoEmbeddable;
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
                .videoEmbeddable(videoEmbeddable)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .tags(tags)
                .build();
    }

}
