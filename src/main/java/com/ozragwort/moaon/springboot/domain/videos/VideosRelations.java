package com.ozragwort.moaon.springboot.domain.videos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "videos_relations",
        uniqueConstraints = {
                @UniqueConstraint(name = "VIDEOID_RELATED_UNIQUE", columnNames = {"videos_relations_video_id", "related_videos"})
        }
)
public class VideosRelations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(name = "videos_relations_video_id", nullable = false)
    private String videoId;

    @JsonBackReference
    @ManyToOne(targetEntity = Videos.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "related_videos", referencedColumnName = "videos_idx", nullable = false)
    private Videos relatedVideos;

    @Builder
    public VideosRelations(String videoId,
                         Videos relatedVideos) {
        this.videoId = videoId;
        this.relatedVideos = relatedVideos;
    }

    public void update(String videoId,
                       Videos relatedVideos) {
        this.videoId = videoId;
        this.relatedVideos = relatedVideos;
    }

}
