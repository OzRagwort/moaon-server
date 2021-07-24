package com.ozragwort.moaon.springboot.dto.videos;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
public class VideosSnippetUpdateRequestDto {

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

    private List<String> tags;

    public VideosSnippetUpdateRequestDto() {
        this.tags = new ArrayList<>();
    }

    public VideosSnippetUpdateRequestDto(String videosName,
                                         String videosThumbnail,
                                         String videosDescription,
                                         String videosPublishedDate,
                                         String videosDuration,
                                         List<String> tags) {
        this.videosName = videosName;
        this.videosThumbnail = videosThumbnail;
        this.videosDescription = videosDescription;
        this.videosPublishedDate = videosPublishedDate;
        this.videosDuration = videosDuration;
        this.tags = tags;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
