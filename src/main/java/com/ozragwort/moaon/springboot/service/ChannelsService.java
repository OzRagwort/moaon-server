package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChannelsService {

    private final ChannelsRepository channelsRepository;

    private final CategoriesRepository categoriesRepository;

    @Transactional
    public String save(ChannelsSaveRequestDto requestDto) {

        if (channelsRepository.findByChannelId(requestDto.getChannelId()) != null)
            throw new IllegalArgumentException("Not empty = " + requestDto.getChannelId());
        Categories categories = categoriesRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category = " + requestDto.getCategoryId()));

        Channels channels = Channels.builder()
                .categories(categories)
                .channelId(requestDto.getChannelId())
                .channelName(requestDto.getChannelName())
                .channelThumbnail(requestDto.getChannelThumbnail())
                .uploadsList(requestDto.getUploadsList())
                .subscribers(requestDto.getSubscribers())
                .bannerExternalUrl(requestDto.getBannerExternalUrl())
                .build();

        return channelsRepository.save(channels).getChannelId();
    }

    @Transactional
    public String update(String channelId, ChannelsUpdateRequestDto requestDto) {
        Channels channels = channelsRepository.findByChannelId(channelId);

        channels.update(requestDto.getChannelName(),
                requestDto.getChannelThumbnail(),
                requestDto.getUploadsList(),
                requestDto.getSubscribers(),
                requestDto.getBannerExternalUrl());

        return channels.getChannelId();
    }

    @Transactional
    public List<ChannelsResponseDto> findById(Long idx) {
        Channels channels = channelsRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        List<ChannelsResponseDto> channelsResponseDtos = new ArrayList<>();
        channelsResponseDtos.add(new ChannelsResponseDto(channels));

        return channelsResponseDtos;
    }

    @Transactional
    public List<ChannelsResponseDto> findByChannelId(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId);
        List<ChannelsResponseDto> channelsResponseDtos = new ArrayList<>();
        channelsResponseDtos.add(new ChannelsResponseDto(channels));

        return channelsResponseDtos;
    }

    @Transactional
    public List<ChannelsResponseDto> findByCategoryIdx(String categoryId, boolean random, Pageable pageable) {
        List<Long> categoryList = StringToListCategories(categoryId);
        if (random) {
            return channelsRepository.findRandByCategoryIdx(categoriesRepository.findByIdxList(categoryList), pageable).stream()
                    .map(ChannelsResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            return channelsRepository.findByCategoryIdx(categoriesRepository.findByIdxList(categoryList), pageable).stream()
                    .map(ChannelsResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<ChannelsResponseDto> findBySubscribers(int subscribers, boolean subOverUnder, String categoryId, boolean random, Pageable pageable) {
        List<Long> categoryList = StringToListCategories(categoryId);
        List<Categories> categories = new ArrayList<>();
        for (Long category : categoryList) {
            categories.add(categoriesRepository.findById(category)
                    .orElseThrow(() -> new IllegalArgumentException("category is empty id=" + category)));
        }

        List<Channels> channels;
        if (random) {
            channels = subOverUnder ?
                    channelsRepository.findOverRandBySubscribers(subscribers, categories, pageable) :
                    channelsRepository.findUnderRandBySubscribers(subscribers, categories, pageable);
        } else {
            channels = subOverUnder ?
                    channelsRepository.findOverBySubscribers(subscribers, categories, pageable) :
                    channelsRepository.findUnderBySubscribers(subscribers, categories, pageable);
        }

        return channels.stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChannelsResponseDto> findAll() {
        return channelsRepository.findAll().stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChannelsResponseDto> findAll(Pageable pageable) {
        return channelsRepository.findAll(pageable).stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAll() {
        channelsRepository.deleteAll();
    }

    @Transactional
    public String delete(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId);
        channelsRepository.delete(channels);
        return channels.getChannelId();
    }

    private List<Long> StringToListCategories(String categoryId) {
        List<Long> list = new ArrayList<>();
        String[] arr = categoryId.split(",");

        for (String s : arr) {
            list.add(Long.parseLong(s));
        }

        return list;
    }

}
