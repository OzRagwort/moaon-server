package com.ozragwort.moaon.springboot.v1.Service.ChannelsServiceTest;

import com.ozragwort.moaon.springboot.v1.domain.categories.Categories;
import com.ozragwort.moaon.springboot.v1.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.v1.domain.channels.Channels;
import com.ozragwort.moaon.springboot.v1.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.v1.service.ChannelsService;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.ChannelsUpdateRequestDto;
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
public class updateTest {

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
    public void Channels_Service_update_Test() {
        //given
        Long idx;
        String channelId = "UCETBLykCDpfP6L0awBd7Vwg";
        String channelName = "channelName";
        String channelThumbnail = "channelThumbnail";
        String uploadsList = "uploadsList";
        int subscribers = 123;
        String bannerExternalUrl = "bannerExternalUrl";

        ChannelsSaveRequestDto channelsSaveRequestDto = ChannelsSaveRequestDto.builder()
                .channelId(channelId)
                .categoryId(categories.getIdx())
                .build();

        ChannelsUpdateRequestDto dto = new ChannelsUpdateRequestDto(
                channelName,
                channelThumbnail,
                uploadsList,
                subscribers,
                bannerExternalUrl
        );

        //when
        channelsService.save(channelsSaveRequestDto);
        String result = channelsService.update(channelId, dto);

        //then
        Channels Channels = channelsRepository.findByChannelId(result);
        assertThat(Channels.getChannelName()).isEqualTo(channelName);
    }

}
