package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosQuality;
import com.ozragwort.moaon.springboot.domain.videos.VideosQualityRepository;
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
public class VideosQualityService {

    private final VideosRepository videosRepository;
    private final VideosQualityRepository videosQualityRepository;
    private final ChannelsRepository channelsRepository;

    @Transactional
    public String save(VideosQualitySaveRequestDto requestDto) {

        Videos videos = videosRepository.findByVideoId(requestDto.getVideoId());
        if (videos == null)
            throw new IllegalArgumentException("Video is no saved = " + requestDto.getVideoId());
        Channels channels = channelsRepository.findByChannelId(requestDto.getChannelId());
        if (channels == null)
            throw new IllegalArgumentException("Channel is no saved = " + requestDto.getChannelId());
        if (!channels.getChannelId().equals(requestDto.getChannelId()))
            throw new IllegalArgumentException("Input channel id is wrong = " + requestDto.getChannelId());

        VideosQuality videosQuality = VideosQuality.builder()
                .videos(videos)
                .channelId(requestDto.getChannelId())
                .score(requestDto.getScore())
                .build();

        return videosQualityRepository.save(videosQuality).getVideos().getVideoId();
    }

    @Transactional
    public String update(String videoId, VideosQualityUpdateRequestDto requestDto) {
        Videos videos = videosRepository.findByVideoId(videoId);
        if (videos == null)
            throw new IllegalArgumentException("Video is no saved = " + requestDto.getVideoId());

        VideosQuality videosQuality = videosQualityRepository.findByVideoId(videoId);
        videosQuality.update(videos, requestDto.getChannelId(), requestDto.getScore());

        return videosQuality.getVideos().getVideoId();
    }

    @Transactional
    public double getScoreAvg() {
        return videosQualityRepository.getScoreAvg();
    }

    @Transactional
    public double getScoreAvgByChannelId(String channelId) {
        return videosQualityRepository.getScoreAvgByChannelId(channelId);
    }

    @Transactional
    public List<VideosQualityResponseDto> findById(Long idx) {
        VideosQuality videosQuality = videosQualityRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        List<VideosQualityResponseDto> videosQualityResponseDtos = new ArrayList<>();
        videosQualityResponseDtos.add(new VideosQualityResponseDto(videosQuality));

        return videosQualityResponseDtos;
    }

    @Transactional
    public List<VideosQualityResponseDto> findByVideoId(String videoId) {
        VideosQuality videosQuality = videosQualityRepository.findByVideoId(videoId);
        List<VideosQualityResponseDto> videosQualityResponseDtos = new ArrayList<>();
        videosQualityResponseDtos.add(new VideosQualityResponseDto(videosQuality));

        return videosQualityResponseDtos;
    }

    @Transactional
    public List<VideosQualityResponseDto> findByScore(double score, Pageable pageable) {
        return videosQualityRepository.findByScore(score, pageable).stream()
                .map(VideosQualityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosQualityResponseDto> findOverAvgRandByScore(int size) {
        double avg = videosQualityRepository.getScoreAvg();
        return videosQualityRepository.findOverAvgRandByScore(avg, size).stream()
                .map(VideosQualityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosQualityResponseDto> findAll(Pageable pageable) {
        return videosQualityRepository.findAll(pageable).stream()
                .map(VideosQualityResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAll() {
        videosQualityRepository.deleteAll();
    }

    @Transactional
    public String delete(String videoId) {
        VideosQuality videosQuality = videosQualityRepository.findByVideoId(videoId);
        if (videosQuality == null) {
            throw new IllegalArgumentException("id가 없음. id=" + videoId);
        }
        videosQualityRepository.delete(videosQuality);

        return videosQuality.getVideos().getVideoId();
    }

}
