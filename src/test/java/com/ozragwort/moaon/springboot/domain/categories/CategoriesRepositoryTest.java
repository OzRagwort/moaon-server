package com.ozragwort.moaon.springboot.domain.categories;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CategoriesRepositoryTest {

    @Autowired
    CategoriesRepository categoriesRepository;

    @Autowired
    ChannelsRepository channelsRepository;

    @Autowired
    VideosRepository videosRepository;

    @After
    public void cleanup() {
        videosRepository.deleteAll();
        channelsRepository.deleteAll();
        categoriesRepository.deleteAll();
    }

    @Test
    public void cate_chan_videRepository_입력확인() throws Exception {
        //given
        String categoryName = "애완동물";

        String channelId = "XXX";
        String channelName = "크림히어로즈";
        String channelThumbnail = "www";

        String videoId = "videoId";
        String videoName = "videoName";
        String videoThumbnail = "videoThumbnail";
        String videoDescription = "videoDescription";
        String videoPublishedDate = "2020-10-18T01:30:01Z";
        String videoDuration = "PT5M47S";
        boolean videoPublicStatsViewable = true;

//        한국 시간으로 변경하는 방법
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("KST"));
//        Date dateVideoPublishedDate = dateFormat.parse(videoPublishedDate);
//
//        duration 초로 얻는법
//        Duration timeFormat = Duration.parse(strVideoDuration);
//        videoDuration = timeFormat.getSeconds();


        categoriesRepository.save(Categories.builder()
                .categoryName(categoryName)
                .build());

        //when_category
        List<Categories> categoriesList = categoriesRepository.findAllDesc();

        //then_category
        Categories categories = categoriesList.get(0);

        //when_channel
        channelsRepository.save(Channels.builder()
                .categories(categories)
                .channelId(channelId)
                .channelName(channelName)
                .channelThumbnail(channelThumbnail)
                .build());

        List<Channels> channelsList = channelsRepository.findAllDesc();

        Channels channels = channelsList.get(0);

        //when_video
        videosRepository.save(Videos.builder()
                .channels(channels)
                .videoId(videoId)
                .videoName(videoName)
                .videoThumbnail(videoThumbnail)
                .videoDescription(videoDescription)
                .videoPublishedDate(videoPublishedDate)
                .videoDuration(videoDuration)
                .videoPublicStatsViewable(videoPublicStatsViewable)
                .build());

        List<Videos> videosList = videosRepository.findAllDesc();

        Videos videos = videosList.get(0);

        //check
        assertThat(categories.getCategoryName()).isEqualTo(categoryName);

        assertThat(channels.getCategories().getIdx()).isEqualTo(categoriesList.get(0).getIdx());
        assertThat(channels.getCategories().getIdx()).isEqualTo(categoriesRepository.findById(1L).get().getIdx());
        assertThat(channels.getCategories().getIdx()).isEqualTo(categoriesRepository.findOne(1L).getIdx());
        assertThat(channels.getChannelName()).isEqualTo(channelName);

        assertThat(videos.getChannels().getIdx()).isEqualTo(channelsList.get(0).getIdx());
        assertThat(videos.getChannels().getIdx()).isEqualTo(channelsRepository.findById(1L).get().getIdx());
        assertThat(videos.getChannels().getIdx()).isEqualTo(channelsRepository.findOne(1L).getIdx());
        assertThat(videos.isVideoPublicStatsViewable()).isEqualTo(videoPublicStatsViewable);

    }

}
