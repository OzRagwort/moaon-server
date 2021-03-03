package com.ozragwort.moaon.springboot.domain.videos;

import com.ozragwort.moaon.springboot.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "videos_tags_popularity",
        uniqueConstraints = @UniqueConstraint(columnNames = {"videos_tags_popularity_categories","videos_tags_popularity_tags"})
)
public class VideosTagsPopularity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "videos_tags_popularity_idx")
    private Long idx;

    @Column(name = "videos_tags_popularity_categories")
    private Long categoryId;

    @Column(name = "videos_tags_popularity_tags")
    private String tags;

    @Builder
    public VideosTagsPopularity(Long categoryId, String tags) {
        this.categoryId = categoryId;
        this.tags = tags;
    }

    public void update(Long categoryId, String tags) {
        this.categoryId = categoryId;
        this.tags = tags;
    }

}
