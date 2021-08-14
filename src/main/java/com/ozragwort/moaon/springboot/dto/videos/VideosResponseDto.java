package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationSecondToString;

@Getter
@NoArgsConstructor
public class VideosResponseDto {

    private Long idx;
    private ChannelsResponseDto channels;
    private String videoId;
    private String videoName;
    private String videoThumbnail;
    private String videoDescription;
    private LocalDateTime videoPublishedDate;
    private String videoDuration;
    private List<String> tags;
    private int viewCount;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private double score;

    public VideosResponseDto(Videos videos) {
        this.idx = videos.getIdx();
        this.channels = new ChannelsResponseDto(videos.getChannels());
        this.videoId = videos.getVideoId();
        this.videoName = videos.getVideoName();
        this.videoThumbnail = videos.getVideoThumbnail();
        this.videoDescription = videos.getVideoDescription();
        this.videoPublishedDate = videos.getVideoPublishedDate();
        this.videoDuration = DurationSecondToString(videos.getVideoDuration());
        this.tags = videos.getTags();
        this.viewCount = videos.getViewCount();
        this.likeCount = videos.getLikeCount();
        this.dislikeCount = videos.getDislikeCount();
        this.commentCount = videos.getCommentCount();
        this.score = videos.getScore();
    }

}
