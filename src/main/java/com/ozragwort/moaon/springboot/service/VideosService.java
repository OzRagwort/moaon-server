package com.ozragwort.moaon.springboot.service;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
import com.ozragwort.moaon.springboot.youtube.YoutubeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideosService {

    private final VideosRepository videosRepository;

    private final ChannelsRepository channelsRepository;

    private final CategoriesRepository categoriesRepository;

    private final YoutubeApi youtubeApi;

    @Transactional
    public Long save(PostVideosRequestDto requestDto) {

        if (videosRepository.findByVideoId(requestDto.getVideoId()) != null) {
            return update(requestDto.getVideoId());
        }

        VideoListResponse videoListResponse = youtubeApi.getVideoListResponse(requestDto.getVideoId());

        VideosSaveRequestDto videosSaveRequestDto = VideosSaveRequestDto.builder()
                .channels(channelsRepository.findByChannelId(videoListResponse.getItems().get(0).getSnippet().getChannelId()))
                .videoId(videoListResponse.getItems().get(0).getId())
                .videoName(videoListResponse.getItems().get(0).getSnippet().getTitle())
                .videoThumbnail(videoListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl())
                .videoDescription(videoListResponse.getItems().get(0).getSnippet().getDescription())
                .videoPublishedDate(videoListResponse.getItems().get(0).getSnippet().getPublishedAt())
                .videoDuration(videoListResponse.getItems().get(0).getContentDetails().getDuration())
                .videoPublicStatsViewable(videoListResponse.getItems().get(0).getStatus().getPublicStatsViewable())
                .viewCount(videoListResponse.getItems().get(0).getStatistics().getViewCount().intValue())
                .likeCount(videoListResponse.getItems().get(0).getStatistics().getLikeCount().intValue())
                .dislikeCount(videoListResponse.getItems().get(0).getStatistics().getDislikeCount().intValue())
                .commentCount(videoListResponse.getItems().get(0).getStatistics().getCommentCount().intValue())
                .tags(videoListResponse.getItems().get(0).getSnippet().getTags())
                .build();

        return videosRepository.save(videosSaveRequestDto.toEntity()).getIdx();
    }

    // need update
    @Transactional
    public List<Long> saveUploadsListVideos(PostChannelUploadsListDto uploadsListDto) {

        String uploadList = channelsRepository.findByChannelId(uploadsListDto.getChannelId()).getUploadsList();

        List<PlaylistItemListResponse> playlistItemListResponse =
                youtubeApi.getPlaylistItemListResponse(uploadList);

        List<Long> list = new ArrayList<>();

        for (int i = 0 ; i < playlistItemListResponse.size() ; i++) {
            for (int j = 0 ; j < playlistItemListResponse.get(i).getItems().size() ; j++) {

                VideosSaveUploadsListRequestDto videosSaveUploadsListRequestDto = VideosSaveUploadsListRequestDto.builder()
                        .channels(channelsRepository.findByChannelId(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getChannelId()))
                        .videoId(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getResourceId().getVideoId())
                        .videoName(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getTitle())
                        .videoThumbnail(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getThumbnails().getMedium().getUrl())
                        .videoDescription(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getDescription())
                        .videoPublishedDate(playlistItemListResponse.get(i).getItems().get(j).getSnippet().getPublishedAt())
                        .build();

                list.add(videosRepository.save(videosSaveUploadsListRequestDto.toEntity()).getIdx());
            }
        }

        return list;
    }

    @Transactional
    public Long update(Long idx) {
        Optional<Videos> byId = videosRepository.findById(idx);

        return update(byId.get().getVideoId());
    }

    @Transactional
    public Long update(String videoId) {

        Videos videos = videosRepository.findByVideoId(videoId);

        if (videos == null) {
            return null;
        }

        VideoListResponse videoListResponse = youtubeApi.getVideoListResponse(videoId);

        videos.update(videoListResponse.getItems().get(0).getSnippet().getTitle(),
                videoListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl(),
                videoListResponse.getItems().get(0).getSnippet().getDescription(),
                videoListResponse.getItems().get(0).getSnippet().getPublishedAt(),
                videoListResponse.getItems().get(0).getContentDetails().getDuration(),
                videoListResponse.getItems().get(0).getStatus().getPublicStatsViewable(),
                videoListResponse.getItems().get(0).getStatistics().getViewCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getLikeCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getDislikeCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getCommentCount().intValue(),
                videoListResponse.getItems().get(0).getSnippet().getTags());

        return videos.getIdx();
    }

    @Transactional
    public List<VideosResponseDto> findById(Long idx) {
        Videos videos = videosRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        List<VideosResponseDto> videosResponseDtos = new ArrayList<>();
        videosResponseDtos.add(new VideosResponseDto(videos));

        return videosResponseDtos;
    }

    @Transactional
    public List<VideosResponseDto> findByVideoId(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId);
        List<VideosResponseDto> videosResponseDtos = new ArrayList<>();
        videosResponseDtos.add(new VideosResponseDto(videos));

        return videosResponseDtos;
    }

    @Transactional
    public List<VideosResponseDto> findByChannelId(String channelId, Pageable pageable) {
        return videosRepository.findByChannelId(channelsRepository.findByChannelId(channelId), pageable).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findByChannelIdRand(String channelId, int count) {
        return videosRepository.findRandByChannelId(channelsRepository.findByChannelId(channelId), count).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findByCategoryIdx(Long categoryIdx, Pageable pageable) {
        return videosRepository.findByCategoryIdx(categoriesRepository.findById(categoryIdx).get(), pageable).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findByCategoryIdxRand(Long categoryIdx, int count) {
        return videosRepository.findRandByCategoryIdx(categoriesRepository.findById(categoryIdx).get(), count).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findAll() {
        return videosRepository.findAll().stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findAll(Pageable pageable) {
        return videosRepository.findAll(pageable).stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long idx) {
        Videos videos = videosRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        videosRepository.delete(videos);

        return idx;
    }

}
