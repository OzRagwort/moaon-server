package com.ozragwort.moaon.springboot.domain.videos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "videos")
public class Videos extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videos_idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "channels_idx")
    private Channels channels;

    @Column(name = "video_id", unique = true)
    private String videoId;

    private String videoName;

    private String videoThumbnail;

    @Column(columnDefinition = "TEXT")
    private String videoDescription;

    private String videoPublishedDate;

    private String videoDuration;

    private boolean videoPublicStatsViewable;

    @Column(columnDefinition = "INT default 0")
    private int viewCount;

    @Column(columnDefinition = "INT default 0")
    private int likeCount;

    @Column(columnDefinition = "INT default 0")
    private int dislikeCount;

    @Column(columnDefinition = "INT default 0")
    private int commentCount;

    @ElementCollection(targetClass = String.class)
    private List<String> tags;

    @Builder
    public Videos(Channels channels,
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

    @Builder
    public Videos(Channels channels,
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

    public void update(String videoName,
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

}
