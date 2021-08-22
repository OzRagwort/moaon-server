package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;
import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;

@Getter
@NoArgsConstructor
public class VideosSaveRequestDto {

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

    @NotNull(message = "viewCount must be provided")
    private int viewCount;

    @NotNull(message = "likeCount must be provided")
    private int likeCount;

    @NotNull(message = "dislikeCount must be provided")
    private int dislikeCount;

    @NotNull(message = "commentCount must be provided")
    private int commentCount;

    private List<String> tags;

    @Builder
    public VideosSaveRequestDto(String channelId,
                                String videosId,
                                String videosName,
                                String videosThumbnail,
                                String videosDescription,
                                String videosPublishedDate,
                                String videosDuration,
                                int viewCount,
                                int likeCount,
                                int dislikeCount,
                                int commentCount,
                                List<String> tags) {
        this.channelId = channelId;
        this.videosId = videosId;
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

    public Videos toEntity(Channels channels) {
        double score = calcScore(viewCount, likeCount, dislikeCount, commentCount);
        return Videos.builder()
                .channels(channels)
                .videoId(videosId)
                .videoName(videosName)
                .videoThumbnail(videosThumbnail)
                .videoDescription(videosDescription)
                .videoPublishedDate(StringToUTCDateTime(videosPublishedDate))
                .videoDuration(DurationStringToSecond(videosDuration))
                .viewCount(viewCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .score(score)
                .tags(tags)
                .build();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
