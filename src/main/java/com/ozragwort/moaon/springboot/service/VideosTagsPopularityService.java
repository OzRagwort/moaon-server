package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularity;
import com.ozragwort.moaon.springboot.domain.videos.VideosTagsPopularityRepository;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularityResponseDto;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularitySaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularityUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideosTagsPopularityService {

    private final VideosTagsPopularityRepository videosTagsPopularityRepository;

    @Transactional
    public Long save(VideosTagsPopularitySaveRequestDto requestDto) {
        VideosTagsPopularitySaveRequestDto videosTagsPopularitySaveRequestDto = VideosTagsPopularitySaveRequestDto.builder()
                .categoryId(requestDto.getCategoryId())
                .tags(requestDto.getTags())
                .build();

        return videosTagsPopularityRepository.save(videosTagsPopularitySaveRequestDto.toEntity()).getIdx();
    }

    @Transactional
    public Long update(Long idx, VideosTagsPopularityUpdateRequestDto requestDto) {
        Optional<VideosTagsPopularity> optional = videosTagsPopularityRepository.findById(idx);
        if (optional.isPresent()) {
            VideosTagsPopularity videosTagsPopularity = optional.get();
            videosTagsPopularity.update(requestDto.getCategoryId(), requestDto.getTags());
            return videosTagsPopularity.getIdx();
        } else {
            return null;
        }
    }

    @Transactional
    public VideosTagsPopularityResponseDto findById(Long idx) {
        return videosTagsPopularityRepository.findById(idx)
                .map(VideosTagsPopularityResponseDto::new)
                .orElse(null);
    }

    @Transactional
    public List<VideosTagsPopularityResponseDto> findTagsPopularityByCategoryId(Long categoryId, boolean random) {
        List<VideosTagsPopularityResponseDto> list;
        if (random) {
            list = videosTagsPopularityRepository.findRandByCategoryId(categoryId).stream()
                    .map(VideosTagsPopularityResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            list = videosTagsPopularityRepository.findByCategoryId(categoryId).stream()
                    .map(VideosTagsPopularityResponseDto::new)
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Transactional
    public List<VideosTagsPopularityResponseDto> findByTags(String tags) {
        return videosTagsPopularityRepository.findByTags(tags).stream()
                .map(VideosTagsPopularityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosTagsPopularityResponseDto> findTagsPopularityByTagsAndCategoryId(Long categoryId, String tags) {
        return videosTagsPopularityRepository.findByTagsAndCategoryId(tags, categoryId).stream()
                .map(VideosTagsPopularityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long idx) {
        videosTagsPopularityRepository.deleteById(idx);
    }
}
