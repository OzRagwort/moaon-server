package com.ozragwort.moaon.springboot.Service.ChannelsServiceTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.service.ChannelsService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.PostChannelsSaveRequestDto;
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
    ChannelsService channelsService;

    @Autowired
    ChannelsRepository channelsRepository;

    @Autowired
    CategoriesRepository categoriesRepository;

    public Categories categories;

    @Before
    public void setup() {
        CategoriesSaveRequestDto categoriesSaveRequestDto = new CategoriesSaveRequestDto("categoryName");

        categories = categoriesRepository.save(categoriesSaveRequestDto.toEntity());
    }

    @After
    public void cleanup() {
        channelsRepository.deleteAll();
    }

    @Test
    public void Channels_Service_delete_Test() {
        //given
        Long idx;
        String channelId = "UCETBLykCDpfP6L0awBd7Vwg";

        PostChannelsSaveRequestDto postChannelsSaveRequestDto = PostChannelsSaveRequestDto.builder()
                .channelId(channelId)
                .categoryId(categories.getIdx())
                .build();

        //when
        channelsService.save(postChannelsSaveRequestDto);
        channelsService.delete(channelId);

        //then
        assertThat(channelsRepository.findAll().size()).isEqualTo(0);
    }

}
