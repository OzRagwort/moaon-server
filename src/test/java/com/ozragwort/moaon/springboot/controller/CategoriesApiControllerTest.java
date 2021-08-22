package com.ozragwort.moaon.springboot.controller;

import com.ozragwort.moaon.springboot.service.categories.CategoriesService;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CategoriesApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriesService categoriesService;

    @Test
    @DisplayName("[Categories API] idx 찾기")
    public void findByIdTest() throws Exception {
        // given
        long idx = 1;

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/categories/" + idx)
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
    @DisplayName("[Categories API] 카테고리의 채널 가져오기")
    public void findChannelsByIdTest() throws Exception {
        // given
        long idx = 1;
        String random = "true";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/categories/" + idx + "/channels?random=true&page=0&size=3")
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
    @DisplayName("[Categories API] 카테고리의 영상 가져오기")
    public void findVideosByIdTest() throws Exception {
        // given
        long idx = 1;
        String random = "true";

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/categories/" + idx + "/videos?random=true&page=0&size=3")
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
    @DisplayName("[Categories API] 전체 찾기")
    public void findAllTest() throws Exception {
        // given

        // when
        ResultActions actions = mockMvc
                .perform(get("/api/moaon/v2/categories")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(notNullValue())))
                .andExpect(jsonPath("$.error", is(nullValue())))
                .andDo(MockMvcResultHandlers.print());
    }

}
