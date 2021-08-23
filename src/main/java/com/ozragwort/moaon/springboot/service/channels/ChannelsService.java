package com.ozragwort.moaon.springboot.service.channels;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ozragwort.moaon.springboot.domain.specs.ChannelsSpecs.SearchKey;
import static com.ozragwort.moaon.springboot.domain.specs.ChannelsSpecs.searchWith;
import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class ChannelsService {

    private final CategoriesRepository categoriesRepository;
    private final ChannelsRepository channelsRepository;
    private final VideosRepository videosRepository;

    @Transactional
    public ChannelsResponseDto save(ChannelsSaveRequestDto requestDto) {
        checkArgument(
                isNull(
                        channelsRepository.findByChannelId(requestDto.getChannelId()).orElse(null)
                ), "Already saved. Channel ID : " + requestDto.getChannelId()
        );

        Categories categories = categoriesRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("No Category found. Category ID " + requestDto.getCategoryId()));

        return new ChannelsResponseDto(channelsRepository.save(requestDto.toEntity(categories)));
    }

    @Transactional
    public ChannelsResponseDto update(String channelId, ChannelsUpdateRequestDto requestDto) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElseThrow(() -> new NoSuchElementException("No Channel found. Channel ID : " + channelId));

        channels.update(
                requestDto.getChannelName(),
                requestDto.getChannelThumbnail(),
                requestDto.getUploadsList(),
                requestDto.getSubscribers(),
                requestDto.getBannerExternalUrl()
        );

        return new ChannelsResponseDto(channels);
    }

    @Transactional
    public ChannelsResponseDto findById(Long idx) {
        Channels channels = channelsRepository.findById(idx)
                .orElse(null);
        return channels == null
                ? null
                : new ChannelsResponseDto(channels);
    }

    @Transactional
    public ChannelsResponseDto findByChannelId(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElse(null);
        return channels == null
                ? null
                : new ChannelsResponseDto(channels);
    }

    @Transactional
    public List<ChannelsResponseDto> findAll(Pageable pageable) {
        return channelsRepository.findAll(pageable).stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChannelsResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        Map<SearchKey, Object> searchKeyword = makeSpecKey(keyword);

        if (searchKeyword.containsKey(SearchKey.CATEGORYID)) {
            String str = (String) searchKeyword.get(SearchKey.CATEGORYID);
            List<Categories> list = Arrays.stream(str.split(",")).map(s -> categoriesRepository.findById(Long.parseLong(s))
                    .orElse(null)).collect(Collectors.toList());
            searchKeyword.put(SearchKey.valueOf("CATEGORYID"), list);
        }

        Specification<Channels> channelsSpecification = searchWith(searchKeyword);
        return channelsRepository.findAll(channelsSpecification, pageable).stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ChannelsResponseDto> findAllByCategoriesId(Long idx, Map<String, Object> keyword, Pageable pageable) {
        if (keyword.containsKey("CATEGORYID")) {
            if (keyword.get("CATEGORYID") != idx) {
                throw new IllegalArgumentException("Invalid arguments. categoryid : " + keyword.get("CATEGORYID"));
            }
        } else {
            keyword.put("CATEGORYID", Long.toString(idx));
        }

        return findAll(keyword, pageable);
    }

    @Transactional
    public List<String> findTagsByChannelId(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElse(null);

        return channels == null
                ? null
                : videosRepository.getTagsByChannelId(channels)
                        .orElse(null);
    }

    @Transactional
    public void delete(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElseThrow(() -> new NoSuchElementException("No Channels found. Channel ID : " + channelId));

        List<Videos> videosList = videosRepository.findAllByChannels(channels)
                .orElseThrow(() -> new IllegalArgumentException("Can't delete Channel. Videos remain on Channel"));

        channelsRepository.delete(channels);
    }

    private Map<SearchKey, Object> makeSpecKey(Map<String, Object> keyword) {
        Map<SearchKey, Object> searchKeyword = new HashMap<>();
        for (String key : keyword.keySet()) {
            searchKeyword.put(SearchKey.valueOf(key.toUpperCase()), keyword.get(key));
        }
        return searchKeyword;
    }

}
