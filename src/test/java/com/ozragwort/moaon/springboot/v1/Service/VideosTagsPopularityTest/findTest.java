package com.ozragwort.moaon.springboot.v1.Service.VideosTagsPopularityTest;

import com.ozragwort.moaon.springboot.v1.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.v1.service.VideosTagsPopularityService;
import com.ozragwort.moaon.springboot.v1.web.dto.*;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class findTest {

    @Autowired
    VideosTagsPopularityService videosTagsPopularityService;

    @Autowired
    VideosTagsPopularityRepository videosTagsPopularityRepository;

    @After
    public void cleanup() {
        videosTagsPopularityRepository.deleteAll();
    }

    @Test
    public void Videos_Tags_Popularity_Service_find_Test() {
        //given
        Long idx;
        Long categoryId = 1L;
        String tags = "tags";

        VideosTagsPopularitySaveRequestDto dto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        //when
        idx = videosTagsPopularityService.save(dto);
        VideosTagsPopularityResponseDto result = videosTagsPopularityService.findById(idx);

        //then
        assertThat(result.getTags()).isEqualTo(dto.getTags());
    }

}
