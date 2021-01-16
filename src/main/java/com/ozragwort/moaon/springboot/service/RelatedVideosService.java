package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.videos.VideosRelations;
import com.ozragwort.moaon.springboot.domain.videos.VideosRelationsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
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
public class RelatedVideosService {

    private final VideosRelationsRepository relatedVideosRepository;

    private final VideosRepository videosRepository;

    @Transactional
    public List<Long> save(PostRelatedVideosSaveRequestDto requestDto) {

        List<Long> list = new ArrayList<>();

        for (int i = 0 ; i < requestDto.getRelatedVideo().size() ; i++) {
            try {
                Videos relatedVideo = videosRepository.findByVideoId(requestDto.getRelatedVideo().get(i));
                RelatedVideosSaveRequestDto relatedVideosSaveRequestDto = RelatedVideosSaveRequestDto.builder()
                        .videoId(requestDto.getVideoId())
                        .relatedVideos(relatedVideo)
                        .build();
                list.add(relatedVideosRepository.save(relatedVideosSaveRequestDto.toEntity()).getIdx());
            } catch (Exception ignored) {

            }
        }

        return list;
    }

    @Transactional
    public Long update(Long idx, RelatedVideosUpdateRequestDto requestDto) {
        VideosRelations relatedVideos = relatedVideosRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
//        Videos updateVideo = videosRepository.findByVideoId(requestDto.getVideoId());
        Videos updateRelatedVideo = videosRepository.findByVideoId(requestDto.getRelatedVideo());
        if (updateRelatedVideo != null) {
            relatedVideos.update(requestDto.getVideoId(), updateRelatedVideo);
            return idx;
        } else {
            return -1L;
        }
    }

    @Transactional
    public List<RelatedVideosResponseDto> findById(Long idx) {
        VideosRelations relatedVideos = relatedVideosRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));

        List<RelatedVideosResponseDto> relatedVideosResponseDtos = new ArrayList<>();
        relatedVideosResponseDtos.add(new RelatedVideosResponseDto(relatedVideos));

        return relatedVideosResponseDtos;
    }

    @Transactional
    public List<RelatedVideosResponseDto> findByVideoId(String videoId) {
        return relatedVideosRepository.findByVideoId(videoId).stream()
                .map(RelatedVideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<RelatedVideosResponseDto> findAll(Pageable pageable) {
        return relatedVideosRepository.findAll(pageable).stream()
                .map(RelatedVideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long idx) {
        VideosRelations relatedVideos = relatedVideosRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        relatedVideosRepository.delete(relatedVideos);

        return idx;
    }

}
