package com.ozragwort.moaon.springboot.service.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.*;
import com.ozragwort.moaon.springboot.dto.videos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ozragwort.moaon.springboot.domain.specs.VideosSpecs.searchWith;
import static com.ozragwort.moaon.springboot.domain.specs.VideosSpecs.VideosSearchKey;
import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;
import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class VideosService {

    private final VideosRepository videosRepository;
    private final ChannelsRepository channelsRepository;
    private final CategoriesRepository categoriesRepository;

    @Transactional
    public VideosResponseDto save(VideosSaveRequestDto requestDto) {
        checkArgument(
                isNull(
                        videosRepository.findByVideoId(requestDto.getVideosId()).orElse(null)
                ), "Already saved. Video ID : " + requestDto.getVideosId()
        );

        Channels channels = channelsRepository.findByChannelId(requestDto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("No Channel found. Channel ID : " + requestDto.getChannelId()));

        Videos savedVideo = videosRepository.save(requestDto.toEntity(channels));

        return new VideosResponseDto(savedVideo);
    }

    @Transactional
    public VideosResponseDto update(String videoId, VideosUpdateRequestDto requestDto) {
        Videos videos = videosRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        double score = calcScore(requestDto.getViewCount(),
                requestDto.getLikeCount(),
                requestDto.getDislikeCount(),
                requestDto.getCommentCount());

        videos.update(
                requestDto.getVideosName(),
                requestDto.getVideosThumbnail(),
                requestDto.getVideosDescription(),
                StringToUTCDateTime(requestDto.getVideosPublishedDate()),
                DurationStringToSecond(requestDto.getVideosDuration()),
                requestDto.getViewCount(),
                requestDto.getLikeCount(),
                requestDto.getDislikeCount(),
                requestDto.getCommentCount(),
                score,
                requestDto.getTags()
        );

        return new VideosResponseDto(videos);
    }

    @Transactional
    public VideosResponseDto findById(Long idx) {
        Videos videos = videosRepository.findById(idx)
                .orElse(null);

        return videos == null
                ? null
                : new VideosResponseDto(videos);
    }

    @Transactional
    public VideosResponseDto findByVideoId(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId)
                .orElse(null);

        return videos == null
                ? null
                : new VideosResponseDto(videos);
    }

    @Transactional
    public List<VideosResponseDto> findAll(Pageable pageable) {
        return videosRepository.findAll(pageable).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        Map<VideosSearchKey, Object> searchKeyword = makeSpecKey(keyword);

        Specification<Videos> videosSpecification = searchWith(searchKeyword);
        return videosRepository.findAll(videosSpecification, pageable).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findAllByChannelsId(String channelId, Map<String, Object> keyword, Pageable pageable) {
        if (keyword.containsKey("CHANNELID")) {
            if (keyword.get("CHANNELID") != channelId) {
                throw new IllegalArgumentException("");
            }
        } else {
            keyword.put("CHANNELID", channelId);
        }

        return findAll(keyword, pageable);
    }

    @Transactional
    public List<VideosResponseDto> findAllByCategoriesId(Long categoriesId, Map<String, Object> keyword, Pageable pageable) {
        if (keyword.containsKey("CATEGORYID")) {
            if (keyword.get("CATEGORYID") != categoriesId) {
                throw new IllegalArgumentException("");
            }
        } else {
            keyword.put("CATEGORYID", categoriesId);
        }

        return findAll(keyword, pageable);
    }

    @Transactional
    public List<String> findTagsByCategoryId(Long idx, Pageable pageable) {
        Categories categories = categoriesRepository.findById(idx)
                .orElseThrow(() -> new NoSuchElementException("No Category found. Category ID : " + idx));

        return videosRepository.getTagsByCategoryId(categories, pageable);
    }

    @Transactional
    public void delete(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        videosRepository.delete(videos);
    }

    private Map<VideosSearchKey, Object> makeSpecKey(Map<String, Object> keyword) {
        Map<VideosSearchKey, Object> searchKeyword = new HashMap<>();

        List<String> searchKeys = new ArrayList<String>(){{
            this.addAll(
                    Arrays.stream(VideosSearchKey.values()).map(VideosSearchKey::toString).collect(Collectors.toList())
            );
        }};

        for (String key : keyword.keySet()) {
            if (searchKeys.contains(key.toUpperCase())) {
                searchKeyword.put(VideosSearchKey.valueOf(key.toUpperCase()), keyword.get(key));
            }
        }

        if (searchKeyword.containsKey(VideosSearchKey.CHANNELID)) {
            String str = (String) searchKeyword.get(VideosSearchKey.CHANNELID);
            List<Channels> list = Arrays.stream(str.split(",")).map(s -> channelsRepository.findByChannelId(s)
                    .orElse(null)).collect(Collectors.toList());
            searchKeyword.put(VideosSearchKey.valueOf("CHANNELID"), list);
        }
        if (searchKeyword.containsKey(VideosSearchKey.CATEGORYID)) {
            String str = (String) searchKeyword.get(VideosSearchKey.CATEGORYID);
            List<Categories> list = Arrays.stream(str.split(",")).map(s -> categoriesRepository.findById(Long.parseLong(s))
                    .orElse(null)).collect(Collectors.toList());
            searchKeyword.put(VideosSearchKey.valueOf("CATEGORYID"), list);
        }
        if (searchKeyword.containsKey(VideosSearchKey.TAGS)) {
            List<String> tags = new ArrayList<String>(){{add((String) searchKeyword.get(VideosSearchKey.TAGS));}};
            searchKeyword.put(VideosSearchKey.TAGS, tags);
        }

        return searchKeyword;
    }

}
