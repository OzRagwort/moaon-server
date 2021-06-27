package com.ozragwort.moaon.springboot.v1.web.dto;

import com.ozragwort.moaon.springboot.v1.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RelatedVideosResponseDto {

    ChannelsResponseDto channels;
    private String videoId;
    private String videoName;
    private String videoThumbnail;
    private LocalDateTime videoPublishedDate;

    @Builder
    public RelatedVideosResponseDto(Videos videos) {
        this.channels = new ChannelsResponseDto(videos.getChannels());
        this.videoId = videos.getVideoId();
        this.videoName = videos.getVideoName();
        this.videoThumbnail = videos.getVideoThumbnail();
        this.videoPublishedDate = videos.getVideoPublishedDate();
    }

}
