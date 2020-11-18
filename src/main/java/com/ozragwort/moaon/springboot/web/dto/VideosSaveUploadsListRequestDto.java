package com.ozragwort.moaon.springboot.web.dto;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VideosSaveUploadsListRequestDto {

    private Channels channels;

    private String videoId;

    private String videoName;

    private String videoThumbnail;

    private String videoDescription;

    private String videoPublishedDate;

    @Builder
    public VideosSaveUploadsListRequestDto(Channels channels,
                                           String videoId,
                                           String videoName,
                                           String videoThumbnail,
                                           String videoDescription,
                                           String videoPublishedDate) {
        this.channels = channels;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
    }

    public Videos toEntity() {
        return Videos.builder()
                .channels(channels)
                .videoId(videoId)
                .videoName(videoName)
                .videoThumbnail(videoThumbnail)
                .videoDescription(videoDescription)
                .videoPublishedDate(videoPublishedDate)
                .build();
    }

}
