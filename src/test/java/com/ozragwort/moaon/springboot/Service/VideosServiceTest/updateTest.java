package com.ozragwort.moaon.springboot.Service.VideosServiceTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.PostVideosRequestDto;
import com.ozragwort.moaon.springboot.web.dto.VideosUpdateRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class updateTest {

    @Autowired
    VideosService videosService;

    @Autowired
    VideosRepository videosRepository;

    @Autowired
    ChannelsRepository channelsRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    public Channels channels;

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

        ChannelsSaveRequestDto channelsSaveRequestDto = new ChannelsSaveRequestDto(
                categories,
                channelId,
                channelName,
                channelThumbnail,
                uploadsList,
                subscribers,
                bannerExternalUrl);

        channels = channelsRepository.save(channelsSaveRequestDto.toEntity());
    }

    @After
    public void cleanup() {
        videosRepository.deleteAll();
        channelsRepository.deleteAll();
        categoriesRepository.deleteAll();
    }

    @Test
    public void Videos_Service_update_Test() {
        //given
        Long idx;
        String videoId = "8vAsg37pyC8";
        String videoName = "videoName";
        String videoThumbnail = "videoThumbnail";
        String videoDescription = "videoDescription";
        LocalDateTime videoPublishedDate = LocalDateTime.of(2020,1,1,0,0,0);
        String videoDuration = "videoDuration";
        boolean videoEmbeddable = true;
        int viewCount = 0;
        int likeCount = 0;
        int dislikeCount = 0;
        int commentCount = 0;
        List<String> tags = new ArrayList<>();

        PostVideosRequestDto postVideosRequestDto = PostVideosRequestDto.builder()
                .videoId(videoId)
                .build();

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

        //when
        videosService.save(postVideosRequestDto);
        String result = videosService.update(videoId, dto);

        //then
        Videos videos = videosRepository.findByVideoId(result);
        assertThat(videos.getVideoName()).isEqualTo(videoName);
    }

}
