package com.ozragwort.moaon.springboot.dto.videos;

import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;
import com.ozragwort.moaon.springboot.domain.videos.VideosStatistics;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;

@Getter
@NoArgsConstructor
public class VideosStatisticsSaveRequestDto {

    @NotNull(message = " must be provided")
    private int viewCount;

    @NotNull(message = " must be provided")
    private int likeCount;

    @NotNull(message = " must be provided")
    private int dislikeCount;

    @NotNull(message = " must be provided")
    private int commentCount;

    public VideosStatisticsSaveRequestDto(int viewCount,
                                          int likeCount,
                                          int dislikeCount,
                                          int commentCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
    }

    public VideosStatistics toEntity(VideosSnippet videos) {
        return VideosStatistics.builder()
                .videosSnippet(videos)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .score(calcScore(viewCount, likeCount, dislikeCount, commentCount))
                .build();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
