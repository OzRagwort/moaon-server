package com.ozragwort.moaon.springboot.controller;

import com.ozragwort.moaon.springboot.dto.videos.*;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class VideosApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VideosService videosService;

    @Test
    @DisplayName("[Videos APi] 저장")
    public void saveTest() throws Exception {
        // given
        String channelId = "testChannelId1";
        String videosId = "saveVideosId";
        String videosName = "saveVideosName";
        String videosThumbnail = "saveVideosThumbnail";
        String videosDescription = "saveVideosDescription";
        String videosPublishedDate = "2021-01-02T00:00:00Z";
        String videosDuration = "PT3M2S";
        List<String> tags = new ArrayList<String>(){
            {
                add("saveTags1");
                add("saveTags2");
                add("saveTags3");
            }
        };
        int viewCount = 1;
        int likeCount = 2;
        int dislikeCount = 3;
        int commentCount = 4;
        VideosSnippetSaveRequestDto snippet = new VideosSnippetSaveRequestDto(
                channelId,
                videosId,
                videosName,
                videosThumbnail,
                videosDescription,
                videosPublishedDate,
                videosDuration,
                tags
        );
        VideosStatisticsSaveRequestDto statistics = new VideosStatisticsSaveRequestDto(
                viewCount,
                likeCount,
                dislikeCount,
                commentCount
        );
        VideosSaveRequestDto requestDto = new VideosSaveRequestDto(snippet, statistics);

        String content = ToStringBuilder.reflectionToString(requestDto, ToStringStyle.JSON_STYLE);

        // when
        ResultActions actions = mockMvc
                .perform(post("/api/moaon/v2/videos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.videoId", is(videosId)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] 수정")
    public void updateTest() throws Exception {
        // given
        String videoId = "testVideoId1";
        String videosName = "updateVideosName";
        String videosThumbnail = "updateVideosThumbnail";
        String videosDescription = "updateVideosDescription";
        String videosPublishedDate = "2021-02-03T01:01:01Z";
        String videosDuration = "PT3M2S";
        List<String> tags = new ArrayList<String>(){
            {
                add("updateTags1");
                add("updateTags2");
                add("updateTags3");
            }
        };
        int viewCount = 1;
        int likeCount = 2;
        int dislikeCount = 3;
        int commentCount = 4;
        VideosSnippetUpdateRequestDto snippet = new VideosSnippetUpdateRequestDto(
                videosName,
                videosThumbnail,
                videosDescription,
                videosPublishedDate,
                videosDuration,
                tags
        );
        VideosStatisticsUpdateRequestDto statistics = new VideosStatisticsUpdateRequestDto(
                viewCount,
                likeCount,
                dislikeCount,
                commentCount
        );
        VideosUpdateRequestDto requestDto = new VideosUpdateRequestDto(snippet, statistics);

        String content = ToStringBuilder.reflectionToString(requestDto, ToStringStyle.JSON_STYLE);

        // when
        ResultActions actions = mockMvc
                .perform(put("/api/moaon/v2/videos/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.videoName", is(videosName)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] videoId 조회")
    public void findByIdTest() throws Exception {
        // given
        String videoId = "testVideoId1";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/videos/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.videoId", is(videoId)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] tags 검색")
    public void findByTagsTest() throws Exception {
        // given
        String tag = "animal";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/videos?tags=" + tag)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(hasSize(1))))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] videos의 statistics 조회")
    public void findStatisticsByVideoIdTest() throws Exception {
        // given
        String videoId = "testVideoId4";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/videos/" + videoId + "/statistics")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.viewCount", is(4)))
                .andExpect(jsonPath("$.response.likeCount", is(4)))
                .andExpect(jsonPath("$.response.dislikeCount", is(4)))
                .andExpect(jsonPath("$.response.commentCount", is(4)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] videos pageable test")
    public void findOrderByRandomTest() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/videos?size=2&sort=idx,asc")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(hasSize(2))))
                .andExpect(jsonPath("$.response.[0].idx", is(1)))
                .andExpect(jsonPath("$.response.[1]idx", is(2)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos APi] 전체 조회")
    public void findAllTest() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/videos")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Videos API] 삭제")
    public void deleteTest() throws Exception {
        // given
        String videoId = "testVideoId4";

        // when
        ResultActions deleteActions = mockMvc
                .perform(delete("/api/moaon/v2/videos/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON));

        ResultActions checkActions = mockMvc
                .perform(get("/api/moaon/v2/videos/" + videoId)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        deleteActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());

        checkActions
                .andExpect(jsonPath("$.response", is(nullValue())))
                .andDo(print());
    }

}
