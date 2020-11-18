package com.ozragwort.moaon.springboot.web;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.CategoriesUpdateRequestDto;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoriesApiControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriesRepository categoriesRepository;

    private String categoryName1 = "categoryName1";

    @Before
    public void setup() throws JsonProcessingException {
        CategoriesSaveRequestDto categoriesSaveRequestDto = new CategoriesSaveRequestDto(categoryName1);

        categoriesRepository.save(categoriesSaveRequestDto.toEntity());
    }

    @After
    public void cleanup() {
        categoriesRepository.deleteAll();
    }

//    @WithMockUser(roles = "USER")
    @Test
    public void 카테고리_API_save() throws Exception {

        //given
        String categoryName2 = "categoryName2";
        String content = objectMapper.writeValueAsString(new CategoriesSaveRequestDto(categoryName2));

        //when
        mvc
                .perform(post("/api/moaon/v1/categories")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                )
        //then
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        List<Categories> list = categoriesRepository.findAll();

        assertThat(list.get(1).getCategoryName()).isEqualTo(categoryName2);

    }

    @Test
    public void 카테고리_api_get() throws Exception {
        //given

        //when
        mvc
                .perform(get("/api/moaon/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.[0].categoryName").value(categoryName1))
                .andDo(MockMvcResultHandlers.print());

        List<Categories> list = categoriesRepository.findAll();

        assertThat(list.get(0).getCategoryName()).isEqualTo(categoryName1);
    }

    @Test
    public void 카테고리_api_put() throws Exception {
        //given
        String updateName = "updateName";
        String content = objectMapper.writeValueAsString(new CategoriesUpdateRequestDto(updateName));
        Long idx = categoriesRepository.findAll().get(0).getIdx();

        //when
        mvc
                .perform(put("/api/moaon/v1/categories/" + idx)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print());

        List<Categories> list = categoriesRepository.findAll();

        assertThat(list.get(0).getCategoryName()).isEqualTo(updateName);
    }

    @Test
    public void 카테고리_api_delete() throws Exception {
        //given
        Long idx = categoriesRepository.findAll().get(0).getIdx();

        //when
        mvc
                .perform(delete("/api/moaon/v1/categories/" + idx)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(MockMvcResultHandlers.print());

        Optional<Categories> list = categoriesRepository.findById(idx);

        assertThat(list).isEmpty();
    }

}
