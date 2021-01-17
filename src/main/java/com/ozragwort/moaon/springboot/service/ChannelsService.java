package com.ozragwort.moaon.springboot.service;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
import com.ozragwort.moaon.springboot.youtube.YoutubeApi;
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

    private final YoutubeApi youtubeApi;

    @Transactional
    public String save(PostChannelsSaveRequestDto requestDto) {

        if (channelsRepository.findByChannelId(requestDto.getChannelId()) != null) {
            return null;
        }

        ChannelListResponse channelListResponse = youtubeApi.getChannelListResponse(requestDto.getChannelId());

        ChannelsSaveRequestDto channelsSaveRequestDto = ChannelsSaveRequestDto.builder()
                .categories(categoriesRepository.findById(requestDto.getCategoryId()).get())
                .channelId(channelListResponse.getItems().get(0).getId())
                .channelName(channelListResponse.getItems().get(0).getSnippet().getTitle())
                .channelThumbnail(channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl())
                .uploadsList(channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads())
                .subscribers(channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue())
                .bannerExternalUrl(channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl())
                .build();

        return channelsRepository.save(channelsSaveRequestDto.toEntity()).getChannelId();
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
    public String refresh(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId);
        if (channels == null) {
            return null;
        }
        ChannelListResponse channelListResponse = youtubeApi.getChannelListResponse(channelId);
        channels.update(channelListResponse.getItems().get(0).getSnippet().getTitle(),
                channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl(),
                channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads(),
                channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue(),
                channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl());
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
    public List<ChannelsResponseDto> findByCategoryIdx(Long categoryIdx, Pageable pageable) {

        return channelsRepository.findByCategoryIdx(categoriesRepository.findById(categoryIdx).get(), pageable).stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChannelsResponseDto> findByCategoryIdxRand(Long categoryIdx, int count) {
        return channelsRepository.findRandByCategoryIdx(categoriesRepository.findById(categoryIdx).get(), count).stream()
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

}
