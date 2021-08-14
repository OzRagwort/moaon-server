package com.ozragwort.moaon.springboot.dto.videos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Getter
@NoArgsConstructor
public class VideosUpdateRequestDto {

    @NotNull(message = "videosName must be provided")
    private String videosName;

    @NotNull(message = "videosThumbnail must be provided")
    private String videosThumbnail;

    @NotNull(message = "videosDescription must be provided")
    private String videosDescription;

    @NotNull(message = "videosPublishedDate must be provided")
    private String videosPublishedDate;

    @NotNull(message = "videosDuration must be provided")
    private String videosDuration;

    @NotNull(message = "viewCount must be provided")
    @PositiveOrZero(message = "viewCount must be positive or zero")
    private int viewCount;

    @NotNull(message = "likeCount must be provided")
    @PositiveOrZero(message = "likeCount must be positive or zero")
    private int likeCount;

    @NotNull(message = "dislikeCount must be provided")
    @PositiveOrZero(message = "dislikeCount must be positive or zero")
    private int dislikeCount;

    @NotNull(message = "commentCount must be provided")
    @PositiveOrZero(message = "commentCount must be positive or zero")
    private int commentCount;

    private List<String> tags;

    public VideosUpdateRequestDto(String videosName,
                                  String videosThumbnail,
                                  String videosDescription,
                                  String videosPublishedDate,
                                  String videosDuration,
                                  int viewCount,
                                  int likeCount,
                                  int dislikeCount,
                                  int commentCount,
                                  List<String> tags) {
        this.videosName = videosName;
        this.videosThumbnail = videosThumbnail;
        this.videosDescription = videosDescription;
        this.videosPublishedDate = videosPublishedDate;
        this.videosDuration = videosDuration;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.tags = tags;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
