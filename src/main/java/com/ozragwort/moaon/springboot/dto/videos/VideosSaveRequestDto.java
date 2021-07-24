package com.ozragwort.moaon.springboot.dto.videos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class VideosSaveRequestDto {

    @NotNull(message = "snippet must be provided")
    private VideosSnippetSaveRequestDto snippet;

    @NotNull(message = "statistics must be provided")
    private VideosStatisticsSaveRequestDto statistics;

    public VideosSaveRequestDto(VideosSnippetSaveRequestDto snippet,
                                VideosStatisticsSaveRequestDto statistics) {
        this.snippet = snippet;
        this.statistics = statistics;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
