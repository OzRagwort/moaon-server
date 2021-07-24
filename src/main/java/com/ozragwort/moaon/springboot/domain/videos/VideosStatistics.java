package com.ozragwort.moaon.springboot.domain.videos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "videos_statistics")
public class VideosStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videos_statistics_idx")
    private Long idx;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "videos_snippet_idx")
    private VideosSnippet videosSnippet;

    @Column(name = "videos_statistics_view_count", columnDefinition = "INT default 0")
    private int viewCount;

    @Column(name = "videos_statistics_like_count", columnDefinition = "INT default 0")
    private int likeCount;

    @Column(name = "videos_statistics_dislike_count", columnDefinition = "INT default 0")
    private int dislikeCount;

    @Column(name = "videos_statistics_comment_count", columnDefinition = "INT default 0")
    private int commentCount;

    @Column(name = "videos_statistics_score")
    private double score;

    @Builder
    public VideosStatistics(VideosSnippet videosSnippet,
                  int viewCount,
                  int likeCount,
                  int dislikeCount,
                  int commentCount,
                  double score) {
        this.videosSnippet = videosSnippet;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
    }

    public void update(int viewCount,
                       int likeCount,
                       int dislikeCount,
                       int commentCount,
                       double score) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
        this.score = score;
    }

}
