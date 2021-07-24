package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;
import com.ozragwort.moaon.springboot.domain.videos.VideosStatistics;
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

    public VideosResponseDto(VideosSnippet videosSnippet, VideosStatistics videosStatistics) {
        this.idx = videosSnippet.getIdx();
        this.channels = new ChannelsResponseDto(videosSnippet.getChannels());
        this.videoId = videosSnippet.getVideoId();
        this.videoName = videosSnippet.getVideoName();
        this.videoThumbnail = videosSnippet.getVideoThumbnail();
        this.videoDescription = videosSnippet.getVideoDescription();
        this.videoPublishedDate = videosSnippet.getVideoPublishedDate();
        this.videoDuration = DurationSecondToString(videosSnippet.getVideoDuration());
        this.tags = videosSnippet.getTags();
        this.viewCount = videosStatistics.getViewCount();
        this.likeCount = videosStatistics.getLikeCount();
        this.dislikeCount = videosStatistics.getDislikeCount();
        this.commentCount = videosStatistics.getCommentCount();
        this.score = videosStatistics.getScore();
    }

}
