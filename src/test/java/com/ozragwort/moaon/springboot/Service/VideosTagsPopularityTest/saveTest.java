package com.ozragwort.moaon.springboot.Service.VideosTagsPopularityTest;

import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularity;
import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.service.VideosTagsPopularityService;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularitySaveRequestDto;
import org.junit.After;
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
    VideosTagsPopularityService videosTagsPopularityService;

    @Autowired
    VideosTagsPopularityRepository videosTagsPopularityRepository;

    @After
    public void cleanup() {
        videosTagsPopularityRepository.deleteAll();
    }

    @Test
    public void Videos_Tags_Popularity_Service_save_Test() {
        //given
        Long categoryId = 1L;
        String tags = "tags";

        VideosTagsPopularitySaveRequestDto dto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(categoryId)
                .tags(tags)
                .build();

        //when
        Long idx = videosTagsPopularityService.save(dto);

        //then
        VideosTagsPopularity result = videosTagsPopularityRepository.findById(idx).get();
        assertThat(result.getTags()).isEqualTo(tags);
    }

}
