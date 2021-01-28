package com.ozragwort.moaon.springboot.Service.VideosTagsPopularityTest;

import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.service.VideosTagsPopularityService;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularitySaveRequestDto;
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
public class deleteTest {

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
    public void Videos_Tags_Popularity_Service_delete_Test() {
        //given

        //when
        videosTagsPopularityService.delete(idx);

        //then
        assertThat(videosTagsPopularityRepository.findAll().size()).isEqualTo(0);
    }

}
