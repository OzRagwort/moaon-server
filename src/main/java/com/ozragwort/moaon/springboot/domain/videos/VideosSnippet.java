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
@Table(name = "videos_snippet")
public class VideosSnippet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videos_snippet_idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "channels_idx")
    private Channels channels;

    @Column(name = "videos_snippet_id", unique = true)
    private String videoId;

    @Column(name = "videos_snippet_name")
    private String videoName;

    @Column(name = "videos_snippet_thumbnail")
    private String videoThumbnail;

    @Column(name = "videos_snippet_description", columnDefinition = "TEXT")
    private String videoDescription;

    @Column(name = "videos_snippet_published_date")
    private LocalDateTime videoPublishedDate;

    @Column(name = "videos_snippet_duration")
    private long videoDuration;

    @ElementCollection(targetClass = String.class, fetch = FetchType.LAZY)
    @CollectionTable(
            name = "videos_tags",
            joinColumns = @JoinColumn(name = "videos_snippet_idx"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"videos_snippet_idx", "videos_tags_tags"})
    )
    @Column(name = "videos_tags_tags")
    private List<String> tags;

    @Builder
    public VideosSnippet(Channels channels,
                         String videoId,
                         String videoName,
                         String videoThumbnail,
                         String videoDescription,
                         LocalDateTime videoPublishedDate,
                         long videoDuration,
                         List<String> tags) {
        this.channels = channels;
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.tags = tags;
    }

    public void update(String videoName,
                       String videoThumbnail,
                       String videoDescription,
                       LocalDateTime videoPublishedDate,
                       long videoDuration,
                       List<String> tags) {
        this.videoName = videoName;
        this.videoThumbnail = videoThumbnail;
        this.videoDescription = videoDescription;
        this.videoPublishedDate = videoPublishedDate;
        this.videoDuration = videoDuration;
        this.tags.clear();
        this.tags = tags;
    }

}
