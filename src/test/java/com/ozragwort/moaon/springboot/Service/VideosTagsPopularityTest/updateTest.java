package com.ozragwort.moaon.springboot.Service.VideosTagsPopularityTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularity;
import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.service.VideosTagsPopularityService;
import com.ozragwort.moaon.springboot.web.dto.*;
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
    VideosTagsPopularityService videosTagsPopularityService;

    @Autowired
    VideosTagsPopularityRepository videosTagsPopularityRepository;

    Long idx;

    @Before
    public void setup() {
        Long categoryId = 1L;
        String tags = "tags";

        VideosTagsPopularitySaveRequestDto dto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        //when
        idx = videosTagsPopularityService.save(dto);
    }

    @After
    public void cleanup() {
        videosTagsPopularityRepository.deleteAll();
    }

    @Test
    public void Videos_Tags_Popularity_Service_update_Test() {
        //given
        Long categoryId = 2L;
        String tags = "modified";

        VideosTagsPopularityUpdateRequestDto dto = VideosTagsPopularityUpdateRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        //when
        Long updateIdx = videosTagsPopularityService.update(idx, dto);

        //then
        VideosTagsPopularity result = videosTagsPopularityRepository.findById(updateIdx).get();
        assertThat(result.getTags()).isEqualTo(tags);
    }

}
