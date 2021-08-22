package com.ozragwort.moaon.springboot.domain.videos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(name = "videos_id", unique = true)
    private String videoId;

    @Column(name = "videos_name")
    private String videoName;

    @Column(name = "videos_thumbnail")
    private String videoThumbnail;

    @Column(name = "videos_description", columnDefinition = "TEXT")
    private String videoDescription;

    @Column(name = "videos_published_date")
    private LocalDateTime videoPublishedDate;

    @Column(name = "videos_duration")
    private long videoDuration;

    @Column(name = "videos_view_count", columnDefinition = "INT default 0")
    private int viewCount;

    @Column(name = "videos_like_count", columnDefinition = "INT default 0")
    private int likeCount;

    @Column(name = "videos_dislike_count", columnDefinition = "INT default 0")
    private int dislikeCount;

    @Column(name = "videos_comment_count", columnDefinition = "INT default 0")
    private int commentCount;

    @Column(name = "videos_score")
    private double score;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "videos_tags",
            joinColumns = @JoinColumn(name = "videos_idx"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"videos_idx", "videos_tags_tags"})
    )
    @Column(name = "videos_tags_tags")
    private List<String> tags;

    @Builder
    public Videos(Channels channels,
                  String videoId,
                  String videoName,
                  String videoThumbnail,
                  String videoDescription,
                  LocalDateTime videoPublishedDate,
                  long videoDuration,
                  int viewCount,
                  int likeCount,
                  int dislikeCount,
                  int commentCount,
                  double score,
                  List<String> tags) {
        this.channels = channels;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
        this.tags = tags;
    }

    public void update(String videoName,
                       String videoThumbnail,
                       String videoDescription,
                       LocalDateTime videoPublishedDate,
                       long videoDuration,
                       int viewCount,
                       int likeCount,
                       int dislikeCount,
                       int commentCount,
                       double score,
                       List<String> tags) {
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
        this.tags.clear();
        this.tags = tags;
    }

}
