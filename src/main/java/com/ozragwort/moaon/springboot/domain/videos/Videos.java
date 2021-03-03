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

    @Column(name = "video_id", unique = true)
    private String videoId;

    @Column(name = "video_name")
    private String videoName;

    private String videoThumbnail;

    @Column(name = "video_description", columnDefinition = "TEXT")
    private String videoDescription;

    private LocalDateTime videoPublishedDate;

    private String videoDuration;

    private boolean videoEmbeddable;

    @Column(columnDefinition = "INT default 0")
    private int viewCount;

    @Column(columnDefinition = "INT default 0")
    private int likeCount;

    @Column(columnDefinition = "INT default 0")
    private int dislikeCount;

    @Column(columnDefinition = "INT default 0")
    private int commentCount;

    @Column(name = "videos_score")
    private double score;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "videos_tags",
            joinColumns = @JoinColumn(name = "videos_tags_videos_idx"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"videos_tags_videos_idx","videos_tags_tags"})
    )
    @Column(name = "videos_tags_tags")
    private List<String> tags;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "videos_relations",
            joinColumns = @JoinColumn(name = "videos_relations_videos_idx"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"videos_relations_videos_idx","videos_relations_related_videos"})
    )
    @Column(name = "videos_relations_related_videos")
    private List<String> relatedVideos;

    @Builder
    public Videos(Channels channels,
                  String videoId,
                  String videoName,
                  String videoThumbnail,
                  String videoDescription,
                  LocalDateTime videoPublishedDate,
                  String videoDuration,
                  boolean videoEmbeddable,
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
        this.videoEmbeddable = videoEmbeddable;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
        this.tags = tags;
    }

    @Builder
    public Videos(Channels channels,
                  String videoId,
                  String videoName,
                  String videoThumbnail,
                  String videoDescription,
                  LocalDateTime videoPublishedDate) {
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
                       LocalDateTime videoPublishedDate,
                       String videoDuration,
                       boolean videoEmbeddable,
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
        this.videoEmbeddable = videoEmbeddable;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
        this.tags.clear();
        this.tags = tags;
    }

    public void addRelations(List<String> relatedVideos) {
        this.relatedVideos.addAll(relatedVideos);
    }

}
