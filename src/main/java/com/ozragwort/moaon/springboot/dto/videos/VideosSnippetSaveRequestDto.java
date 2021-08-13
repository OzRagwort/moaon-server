package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;

@Getter
public class VideosSnippetSaveRequestDto {

    @NotNull(message = "channelId must be provided")
    private String channelId;

    @NotNull(message = "videosId must be provided")
    private String videosId;

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

    public VideosSnippetSaveRequestDto() {
        this.tags = new ArrayList<>();
    }

    @Builder
    public VideosSnippetSaveRequestDto(String channelId,
                                       String videosId,
                                       String videosName,
                                       String videosThumbnail,
                                       String videosDescription,
                                       String videosPublishedDate,
                                       String videosDuration,
                                       List<String> tags) {
        this.channelId = channelId;
        this.videosId = videosId;
        this.videosName = videosName;
        this.videosThumbnail = videosThumbnail;
        this.videosDescription = videosDescription;
        this.videosPublishedDate = videosPublishedDate;
        this.videosDuration = videosDuration;
        this.tags = tags;
    }

    public VideosSnippet toEntity(Channels channels) {
        return VideosSnippet.builder()
                .channels(channels)
                .videoId(videosId)
                .videoName(videosName)
                .videoThumbnail(videosThumbnail)
                .videoDescription(videosDescription)
                .videoPublishedDate(StringToUTCDateTime(videosPublishedDate))
                .videoDuration(DurationStringToSecond(videosDuration))
                .tags(tags)
                .build();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
