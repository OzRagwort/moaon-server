package com.ozragwort.moaon.springboot.dto.videos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class VideosStatisticsUpdateRequestDto {

    @NotNull(message = "viewCount must be provided")
    @PositiveOrZero(message = "viewCount must be positive or zero")
    private int viewCount;

    @NotNull(message = "likeCount must be provided")
    @PositiveOrZero(message = "likeCount must be positive or zero")
    private int likeCount;

    @NotNull(message = "dislikeCount must be provided")
    @PositiveOrZero(message = "dislikeCount must be positive or zero")
    private int dislikeCount;

    @NotNull(message = "commentCount must be provided")
    @PositiveOrZero(message = "commentCount must be positive or zero")
    private int commentCount;

    public VideosStatisticsUpdateRequestDto(int viewCount,
                                          int likeCount,
                                          int dislikeCount,
                                          int commentCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.commentCount = commentCount;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
