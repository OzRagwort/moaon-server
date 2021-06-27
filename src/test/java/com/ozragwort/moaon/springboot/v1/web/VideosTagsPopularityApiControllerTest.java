package com.ozragwort.moaon.springboot.v1.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozragwort.moaon.springboot.v1.domain.videos.VideosTagsPopularity;
import com.ozragwort.moaon.springboot.v1.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.v1.web.dto.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideosTagsPopularityApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VideosTagsPopularityRepository videosTagsPopularityRepository;

    Long idx;
    Long categoryId = 1L;
    String tags = "tags";

    @Before
    public void setup() {
        Long categoryId = this.categoryId;
        String tags = this.tags;

        VideosTagsPopularitySaveRequestDto dto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        idx = videosTagsPopularityRepository.save(dto.toEntity()).getIdx();
    }

    @After
    public void cleanup() {
        videosTagsPopularityRepository.deleteAll();
    }

    @Test
    public void 인기태그_api_save() throws Exception {
        //given
        Long idx;
        Long categoryId = 5L;
        String tags = "saveTags";

        VideosTagsPopularitySaveRequestDto dto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        String content = objectMapper.writeValueAsString(dto);

        //when
        MvcResult mvcResult = mvc
                .perform(post("/api/moaon/v1/popular-tags")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
                //then
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String returnIdx = mvcResult.getResponse().getContentAsString();

        Optional<VideosTagsPopularity> result = videosTagsPopularityRepository.findById(Long.parseLong(returnIdx));

        assertThat(result).isPresent();
        assertThat(result.get().getTags()).isEqualTo(tags);
    }

    @Test
    public void 인기태그_api_get() throws Exception {
        //given

        //when
        mvc
                .perform(get("/api/moaon/v1/popular-tags/"+idx)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.tags").value(tags))
                .andDo(MockMvcResultHandlers.print());

        Optional<VideosTagsPopularity> list = videosTagsPopularityRepository.findById(idx);

        assertThat(list).isPresent();
        assertThat(list.get().getTags()).isEqualTo(tags);
    }

    @Test
    public void 인기태그_api_put() throws Exception {
        //given
        Long categoryId = 2L;
        String tags = "modified";

        VideosTagsPopularityUpdateRequestDto dto = VideosTagsPopularityUpdateRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        String content = objectMapper.writeValueAsString(dto);

        //when
        mvc
                .perform(put("/api/moaon/v1/popular-tags/"+idx)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Optional<VideosTagsPopularity> list = videosTagsPopularityRepository.findById(idx);

        assertThat(list).isPresent();
        assertThat(list.get().getTags()).isEqualTo(tags);
    }

    @Test
    public void 인기태그_api_delete() throws Exception {
        //given

        //when
        mvc
                .perform(delete("/api/moaon/v1/popular-tags/" + idx)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        assertThat(videosTagsPopularityRepository.findById(idx)).isNotPresent();
    }



}
