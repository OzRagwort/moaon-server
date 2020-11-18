package com.ozragwort.moaon.springboot.Service.Videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.service.ChannelsService;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.PostChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.PostVideosRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class saveTest {

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

        ChannelsSaveRequestDto channelsSaveRequestDto = new ChannelsSaveRequestDto(
                categories,
                channelId,
                channelName,
                channelThumbnail,
                uploadsList,
                subscribers);

        channels = channelsRepository.save(channelsSaveRequestDto.toEntity());
    }

    @After
    public void cleanup() {
        videosRepository.deleteAll();
        channelsRepository.deleteAll();
        categoriesRepository.deleteAll();
    }

    @Test
    public void Videos_Service_save_Test() {
        //given
        String videoId = "8vAsg37pyC8";

        PostVideosRequestDto postVideosRequestDto = PostVideosRequestDto.builder()
                .videoId(videoId)
                .build();

        //when
        videosService.save(postVideosRequestDto);

        //then
        Videos videos = videosRepository.findAll().get(0);
        assertThat(videos.getVideoId()).isEqualTo(videoId);
    }

}
