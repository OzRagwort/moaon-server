package com.ozragwort.moaon.springboot.controller;

import com.ozragwort.moaon.springboot.dto.channels.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsUpdateRequestDto;
import com.ozragwort.moaon.springboot.service.channels.ChannelsService;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ChannelsApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChannelsService channelsService;

    @Test
    @DisplayName("[Channels API] 저장")
    public void saveTest() throws Exception {
        // given
        Long categories_idx = 1L;
        String channels_id = "saveTestId";
        String channels_name = "saveTestName";
        String channels_thumbnail = "saveTestThumbnail";
        String channels_uploads_list = "saveTestUpload";
        int channels_subscribers = 123;
        String channels_banner_external_url = "saveTestBannerUrl";

        ChannelsSaveRequestDto requestDto = new ChannelsSaveRequestDto(
                categories_idx,
                channels_id,
                channels_name,
                channels_thumbnail,
                channels_uploads_list,
                channels_subscribers,
                channels_banner_external_url
        );

        String content = ToStringBuilder.reflectionToString(requestDto, ToStringStyle.JSON_STYLE);

        // when
        ResultActions actions = mockMvc
                .perform(post("/api/moaon/v2/channels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.channelId", is(channels_id)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] 수정")
    public void updateTest() throws Exception {
        // given
        String channelId = "testChannelId1";
        String channels_name = "updateTestName";
        String channels_thumbnail = "updateTestThumbnail";
        String channels_uploads_list = "updateTestUpload";
        int channels_subscribers = 123;
        String channels_banner_external_url = "updateTestBannerUrl";

        ChannelsUpdateRequestDto requestDto = new ChannelsUpdateRequestDto(
                channels_name,
                channels_thumbnail,
                channels_uploads_list,
                channels_subscribers,
                channels_banner_external_url
        );

        String content = ToStringBuilder.reflectionToString(requestDto, ToStringStyle.JSON_STYLE);

        // when
        ResultActions actions = mockMvc
                .perform(put("/api/moaon/v2/channels/" + channelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.channelName", is(channels_name)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] channel id 조회")
    public void findByIdTest() throws Exception {
        // given
        String channelId = "testChannelId1";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/channels/" + channelId)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.response.channelId", is(channelId)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] 채널의 tags 조회")
    public void findTagsByChannelIdTest() throws Exception {
        // given
        String channelId = "testChannelId1";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/channels/" + channelId + "/tags")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(hasSize(3))))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] 특정 채널의 videos snippet 조회")
    public void findSnippetByChannelIdTest() throws Exception {
        // given
        String channelId = "testChannelId1";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/channels/" + channelId + "/videos")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(hasSize(3))))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] 전체 조회")
    public void findAllTest() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/channels?size=5")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(hasSize(5))))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

    @Test
    @DisplayName("[Channels API] 삭제")
    public void deleteTest() throws Exception {
        // given
        String channelId = "testChannelId9";

        // when
        ResultActions actions = mockMvc
                .perform(delete("/api/moaon/v2/channels/" + channelId)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(print());
    }

}
