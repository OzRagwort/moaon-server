package com.ozragwort.moaon.springboot.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ChannelsApiControllerTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private ChannelsRepository channelsRepository;

        @Autowired
        private CategoriesRepository categoriesRepository;

        @Before
        public void setup() throws JsonProcessingException {

                CategoriesSaveRequestDto categoriesSaveRequestDto = new CategoriesSaveRequestDto("categoryName");

                Categories categories = categoriesRepository.save(categoriesSaveRequestDto.toEntity());

                String channelId = "UCnjyiWHGEyww-p8QYSftx2A";
                String channelName = "MochaMilk";
                String channelThumbnail = "https://yt3.ggpht.com/a/AATXAJyt1PtqMTt0Mz3TZSX4Y5RuZICAt08dDf675_eNLg=s240-c-k-c0x00ffffff-no-rj";
//                String uploadsList = "UUnjyiWHGEyww-p8QYSftx2A"; orig
                String uploadsList = "testUploadsList";
                int subscribers = 1180000;

                ChannelsSaveRequestDto channelsSaveRequestDto = new ChannelsSaveRequestDto(
                        categories,
                        channelId,
                        channelName,
                        channelThumbnail,
                        uploadsList,
                        subscribers);

                channelsRepository.save(channelsSaveRequestDto.toEntity());
        }

        @After
        public void cleanup() {
                channelsRepository.deleteAll();
                categoriesRepository.deleteAll();
        }

        //    @WithMockUser(roles = "USER")
        @Test
        public void 채널_api_save() throws Exception {

                //given
                String channelId = "UCETBLykCDpfP6L0awBd7Vwg";
                String channelName = "Arirang은 고양이들내가 주인";
                String channelThumbnail = "https://yt3.ggpht.com/a/AATXAJxxjMYuOcCU7XqT81tYz3Kl-yjimbfCTJMPuKjBDg=s240-c-k-c0x00ffffff-no-rj";
                String uploadsList = "UUETBLykCDpfP6L0awBd7Vwg";
                int subscribers = 568000;

                String content = objectMapper.writeValueAsString(
                        new PostChannelsSaveRequestDto(categoriesRepository.findAll().get(0).getIdx(), channelId));

                //when
                mvc
                        .perform(post("/api/moaon/v1/channels")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                        )
                        //then
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

                Channels channels = channelsRepository.findByChannelId(channelId);

                assertThat(channels.getChannelId()).isEqualTo(channelId);
        }

        @Test
        public void 채널_api_get() throws Exception {
                //given

                //when
                mvc
                        .perform(get("/api/moaon/v1/channels")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.[0].channelId").value("UCnjyiWHGEyww-p8QYSftx2A"))
                        .andDo(MockMvcResultHandlers.print());

                List<Channels> list = channelsRepository.findAll();

                assertThat(list.get(0).getChannelId()).isEqualTo("UCnjyiWHGEyww-p8QYSftx2A");
        }

        @Test
        public void 채널_api_put() throws Exception {
                //given

                //when
                mvc
                        .perform(put("/api/moaon/v1/channels/UCnjyiWHGEyww-p8QYSftx2A")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andDo(MockMvcResultHandlers.print());

                List<Channels> list = channelsRepository.findAll();

                assertThat(list.get(0).getUploadsList()).isNotEqualTo("testUploadsList");
        }

        @Test
        public void 채널_api_delete() throws Exception {
                //given
                ChannelsSaveRequestDto channelsSaveRequestDto = ChannelsSaveRequestDto.builder()
                        .channelId("channelId")
                        .channelName("channelName")
                        .build();

                Long idx = channelsRepository.save(channelsSaveRequestDto.toEntity()).getIdx();

                //when
                mvc
                        .perform(delete("/api/moaon/v1/channels/"+idx)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andDo(MockMvcResultHandlers.print());

                Optional<Channels> channels = channelsRepository.findById(idx);

                assertThat(channels).isEmpty();
        }

}
