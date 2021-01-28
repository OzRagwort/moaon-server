package com.ozragwort.moaon.springboot.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideosApiControllerTest {

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private VideosRepository videosRepository;

        @Autowired
        private ChannelsRepository channelsRepository;

        @Autowired
        private CategoriesRepository categoriesRepository;

        @Before
        public void setup() {

                CategoriesSaveRequestDto categoriesSaveRequestDto = new CategoriesSaveRequestDto("categoryName");

                Categories categories = categoriesRepository.save(categoriesSaveRequestDto.toEntity());

                String channelId = "UCnjyiWHGEyww-p8QYSftx2A";
                String channelName = "MochaMilk";
                String channelThumbnail = "https://yt3.ggpht.com/a/AATXAJyt1PtqMTt0Mz3TZSX4Y5RuZICAt08dDf675_eNLg=s240-c-k-c0x00ffffff-no-rj";
                String uploadsList = "UUnjyiWHGEyww-p8QYSftx2A";
                int subscribers = 1180000;
                String bannerExternalUrl = "bannerExternalUrl";

                Channels channels = new Channels(
                        categories,
                        channelId,
                        channelName,
                        channelThumbnail,
                        uploadsList,
                        subscribers,
                        bannerExternalUrl);

                Channels channels1 = channelsRepository.save(channels);

                Videos videos = Videos.builder()
                        .channels(channels1)
                        .videoId("mGuYJ4wc-xw")
                        .videoName("testName")
                        .build();
                videosRepository.save(videos);
        }

        @After
        public void cleanup() {
                videosRepository.deleteAll();
                channelsRepository.deleteAll();
                categoriesRepository.deleteAll();
        }

        //    @WithMockUser(roles = "USER")
        @Test
        public void 영상_api_save() throws Exception {

                //given
                String channelId = "UCnjyiWHGEyww-p8QYSftx2A";
                String videoId = "ZpIGb7QBhQA";
                String videoName = "videoName";
                String videoThumbnail = "videoThumbnail";
                String videoDescription = "videoDescription";
                String videoPublishedDate = "2021-01-01T00:00:00Z";
                String videoDuration = "videoDuration";
                boolean videoEmbeddable = true;
                int viewCount = 0;
                int likeCount = 0;
                int dislikeCount = 0;
                int commentCount = 0;
                List<String> tags = new ArrayList<>();

                String content = objectMapper.writeValueAsString(
                        VideosSaveRequestDto.builder()
                                .videoId(videoId)
                                .channelId(channelId)
                                .videoName(videoName)
                                .videoThumbnail(videoThumbnail)
                                .videoDescription(videoDescription)
                                .videoPublishedDate(videoPublishedDate)
                                .videoDuration(videoDuration)
                                .videoEmbeddable(videoEmbeddable)
                                .viewCount(viewCount)
                                .likeCount(likeCount)
                                .dislikeCount(dislikeCount)
                                .commentCount(commentCount)
                                .tags(tags)
                                .build()
                );

                //when
                mvc
                        .perform(post("/api/moaon/v1/videos")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                        )
                        //then
                        .andDo(print())
                        .andExpect(status().isOk());

                Videos videos = videosRepository.findByVideoId(videoId);

                assertThat(videos.getVideoId()).isEqualTo(videoId);

        }

        @Test
        public void 영상_api_get() throws Exception {
                //given

                //when
                mvc
                        .perform(get("/api/moaon/v1/videos")
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(jsonPath("$.[0].videoId").value("mGuYJ4wc-xw"))
                        .andDo(MockMvcResultHandlers.print());

                List<Videos> list = videosRepository.findAll();

                assertThat(list.get(0).getVideoId()).isEqualTo("mGuYJ4wc-xw");
        }

        @Test
        public void 영상_api_put() throws Exception {
                //given
                String videoId = "mGuYJ4wc-xw";
                String videoName = "videoName";
                String videoThumbnail = "videoThumbnail";
                String videoDescription = "videoDescription";
                String videoPublishedDate = "2021-01-28T10:00:04Z";
                String videoDuration = "videoDuration";
                boolean videoEmbeddable = true;
                int viewCount = 0;
                int likeCount = 0;
                int dislikeCount = 0;
                int commentCount = 0;
                List<String> tags = new ArrayList<>();

                VideosUpdateRequestDto dto = new VideosUpdateRequestDto(
                        videoName,
                        videoThumbnail,
                        videoDescription,
                        videoPublishedDate,
                        videoDuration,
                        videoEmbeddable,
                        viewCount,
                        likeCount,
                        dislikeCount,
                        commentCount,
                        tags
                );

                String content = objectMapper.writeValueAsString(dto);

                //when
                mvc
                        .perform(put("/api/moaon/v1/videos/"+videoId)
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print());

                List<Videos> list = videosRepository.findAll();

                assertThat(list.get(0).getVideoName()).isNotEqualTo("testName");
        }

        @Test
        public void 영상_api_delete() throws Exception {
                //given
                Videos videos = Videos.builder()
                        .videoId("testId")
                        .videoName("testName")
                        .build();
                String videoId = videosRepository.save(videos).getVideoId();

                //when
                mvc
                        .perform(delete("/api/moaon/v1/videos/" + videoId)
                                .contentType(MediaType.APPLICATION_JSON_UTF8))
                        //then
                        .andExpect(status().isOk())
                        .andDo(MockMvcResultHandlers.print());

                Videos checkVideos = videosRepository.findByVideoId(videoId);

                assertThat(checkVideos).isNull();
        }



}
