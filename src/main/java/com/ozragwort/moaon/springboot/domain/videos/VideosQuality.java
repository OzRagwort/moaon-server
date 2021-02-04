package com.ozragwort.moaon.springboot.domain.videos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "videos_quality")
public class VideosQuality extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videos_quality_idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "videos_idx", unique = true)
    private Videos videos;

    @Column(name = "videos_quality_channelId")
    private String channelId;

    @Column(name = "videos_quality_score")
    private double score;

    @Builder
    public VideosQuality(Videos videos,
                         String channelId,
                         double score) {
        this.videos = videos;
        this.channelId = channelId;
        this.score = score;
    }

    public void update(Videos videos,
                       String channelId,
                       double score) {
        this.videos = videos;
        this.channelId = channelId;
        this.score = score;
    }

}
