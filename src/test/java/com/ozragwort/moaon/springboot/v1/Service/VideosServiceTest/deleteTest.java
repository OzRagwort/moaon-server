package com.ozragwort.moaon.springboot.v1.Service.VideosServiceTest;

import com.ozragwort.moaon.springboot.v1.domain.categories.Categories;
import com.ozragwort.moaon.springboot.v1.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.v1.domain.channels.Channels;
import com.ozragwort.moaon.springboot.v1.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.v1.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.v1.service.VideosService;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.VideosSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class deleteTest {

    @Autowired
    VideosService videosService;

    @Autowired
    VideosRepository videosRepository;

    @Autowired
    ChannelsRepository channelsRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

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

        channelsRepository.save(channels);
    }

    @After
    public void cleanup() {
        videosRepository.deleteAll();
        channelsRepository.deleteAll();
        categoriesRepository.deleteAll();
    }

    @Test
    public void Videos_Service_delete_Test() {
        //given
        Long idx;
        String channelId = "UCnjyiWHGEyww-p8QYSftx2A";
        String videoId = "8vAsg37pyC8";
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

        VideosSaveRequestDto videosSaveRequestDto = VideosSaveRequestDto.builder()
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
                .build();

        //when
        String result = videosService.save(videosSaveRequestDto);
        videosService.delete(videoId);

        //then
        assertThat(videosRepository.findByVideoId(result)).isNull();
    }

}
