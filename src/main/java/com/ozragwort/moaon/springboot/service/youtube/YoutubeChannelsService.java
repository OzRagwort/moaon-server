package com.ozragwort.moaon.springboot.service.youtube;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.videos.*;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import com.ozragwort.moaon.springboot.util.youtube.YoutubeDataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.*;

@RequiredArgsConstructor
@Service
public class YoutubeChannelsService {

    private final CategoriesRepository categoriesRepository;
    private final ChannelsRepository channelsRepository;
    private final VideosRepository videosRepository;
    private final YoutubeVideosService youtubeVideosService;
    private final VideosService videosService;
    private final YoutubeDataApi youtubeDataApi;

    @Transactional
    public ChannelsResponseDto save(AdminChannelsSaveRequestDto requestDto) {
        ChannelListResponse channelListResponse = youtubeDataApi.getChannelListResponse(requestDto.getChannelId(), null);

        if (channelListResponse.getItems().isEmpty()) {
            return null;
        } else {
            Channels channels = channelsRepository.save(responseToEntity(channelListResponse, requestDto.getCategoryId()));
            return new ChannelsResponseDto(channels);
        }
    }

    @Transactional
    public ChannelsResponseDto refresh(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElseThrow(() -> new NoSuchElementException("No Channels found. Channel ID : " + channelId));

        ChannelListResponse channelListResponse = youtubeDataApi.getChannelListResponse(channelId, null);

        if (channelListResponse == null) {
            return new ChannelsResponseDto(channels);
        } else {
            return new ChannelsResponseDto(updateChannels(channelListResponse, channels));
        }
        
    }

    @Transactional
    public void uploadUpdate(String channelId) {
        Channels channels = channelsRepository.findByChannelId(channelId)
                .orElseThrow(() -> new NoSuchElementException("No Channels found. Channel ID : " + channelId));

        List<PlaylistItemListResponse> playlistItemListResponse = youtubeDataApi.getPlaylistItemListResponse(channels.getUploadsList(), null);

        for (PlaylistItemListResponse itemListResponse : playlistItemListResponse) {
            itemListResponse.getItems().stream().map(playlistItem ->
                    VideosSaveRequestDto.builder()
                            .channelId(playlistItem.getSnippet().getChannelId())
                            .videosId(playlistItem.getSnippet().getResourceId().getVideoId())
                            .videosName(playlistItem.getSnippet().getTitle())
                            .videosThumbnail(playlistItem.getSnippet().getThumbnails().getMedium().getUrl())
                            .videosDescription(playlistItem.getSnippet().getDescription())
                            .videosPublishedDate(playlistItem.getSnippet().getPublishedAt())
                            .videosDuration("PT0M0S")
                            .viewCount(0)
                            .likeCount(0)
                            .dislikeCount(0)
                            .commentCount(0)
                            .tags(new ArrayList<>())
                            .build())
                    .forEach(videosSaveRequestDto -> {
                        Videos videos = videosRepository.findByVideoId(videosSaveRequestDto.getVideosId())
                                .orElse(null);

                        if (videos == null) {
                            videosService.save(videosSaveRequestDto);
                        } else {
                            youtubeVideosService.refresh(videosSaveRequestDto.getVideosId());
                        }
                    });
        }
    }

    @Transactional
    public void delete(String channelId) {
        Optional<Channels> optionalChannels = channelsRepository.findByChannelId(channelId);

        if (optionalChannels.isPresent()) {
            Channels channels = optionalChannels.get();

            List<VideosResponseDto> videoList = videosService.findAllByChannelsId(channelId,
                    new HashMap<>(),
                    PageRequest.of(0, 10));
            if (videoList.size() != 0) {
                throw new IllegalArgumentException("Video data remains in Channel. channel : " + channelId);
            }

            channelsRepository.delete(channels);
        }
    }


    private Channels responseToEntity(ChannelListResponse channelListResponse, Long categoryId) {
        Categories categories = categoriesRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category id is incorrect. Category Id : " + categoryId));

        return Channels.builder()
                .categories(categories)
                .channelId(channelListResponse.getItems().get(0).getId())
                .channelName(channelListResponse.getItems().get(0).getSnippet().getTitle())
                .channelThumbnail(channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl())
                .uploadsList(channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads())
                .subscribers(channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue())
                .bannerExternalUrl(channelListResponse.getItems().get(0).getBrandingSettings().isEmpty() ?
                        null : channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl())
                .build();
    }

    private Channels updateChannels(ChannelListResponse channelListResponse, Channels channels) {
        channels.update(channelListResponse.getItems().get(0).getSnippet().getTitle(),
                channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl(),
                channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads(),
                channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue(),
                channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl());
        return channels;
    }

}
