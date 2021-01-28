package com.ozragwort.moaon.springboot.service;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.VideoListResponse;
import com.ozragwort.moaon.springboot.component.ConvertUtcDateTime;
import com.ozragwort.moaon.springboot.component.ModifiedDurationCheck;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.web.dto.*;
import com.ozragwort.moaon.springboot.youtube.YoutubeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class YoutubeService {

    private final VideosRepository videosRepository;

    private final ChannelsRepository channelsRepository;

    private final CategoriesRepository categoriesRepository;

    private final YoutubeApi youtubeApi;

    @Transactional
    public String updateChannelByYoutube(YoutubeChannelsSaveRequestDto requestDto) {
        ChannelListResponse channelListResponse = youtubeApi.getChannelListResponse(requestDto.getChannelId());
        Channels channels = channelsRepository.findByChannelId(requestDto.getChannelId());
        categoriesRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("no category = " + requestDto.getCategoryId()));

        if (channels != null) {
            return updateChannels(channelListResponse, channels);
        } else {
            return saveChannels(channelListResponse, requestDto.getCategoryId());
        }
    }

    @Transactional
    public String updateVideoByYoutube(YoutubeVideosSaveRequestDto requestDto) {
        VideoListResponse videoListResponse = youtubeApi.getVideoListResponse(requestDto.getVideoId());
        Videos videos = videosRepository.findByVideoId(requestDto.getVideoId());

        if (videos != null) {
            ModifiedDurationCheck check = new ModifiedDurationCheck();
            return !check.ModifiedDurationTimeCheck(videos.getModifiedDate()) ?
                    videos.getVideoId() : updateVideos(videoListResponse, videos);
        } else {
            return saveVideos(videoListResponse);
        }
    }

    @Transactional
    public List<Long> updateVideosByUploadsList(YoutubeChannelUploadsListRequestDto requestDto) {
        Channels channels = channelsRepository.findByChannelId(requestDto.getChannelId());
        if (channels == null)
            throw new IllegalArgumentException("no channel = " + requestDto.getChannelId());
        String uploadList = channels.getUploadsList();
        List<PlaylistItemListResponse> playlistItemListResponse = youtubeApi.getPlaylistItemListResponse(uploadList);
        List<Long> list = new ArrayList<>();

        for (PlaylistItemListResponse itemListResponse : playlistItemListResponse) {
            itemListResponse.getItems().stream().map(playlistItem -> VideosSaveUploadsListRequestDto.builder()
                    .channels(channelsRepository.findByChannelId(playlistItem.getSnippet().getChannelId()))
                    .videoId(playlistItem.getSnippet().getResourceId().getVideoId())
                    .videoName(playlistItem.getSnippet().getTitle())
                    .videoThumbnail(playlistItem.getSnippet().getThumbnails().getMedium().getUrl())
                    .videoDescription(playlistItem.getSnippet().getDescription())
                    .videoPublishedDate(ConvertUtcDateTime.StringToUTCDateTime(playlistItem.getSnippet().getPublishedAt()))
                    .build())
                        .map(videosSaveUploadsListRequestDto -> videosRepository.save(videosSaveUploadsListRequestDto.toEntity()).getIdx())
                        .forEach(list::add);
        }

        return list;
    }

    private String saveChannels(ChannelListResponse channelListResponse, Long categoryId) {
        Categories categories = categoriesRepository.findById(categoryId).get();

        Channels channels = Channels.builder()
                .categories(categories)
                .channelId(channelListResponse.getItems().get(0).getId())
                .channelName(channelListResponse.getItems().get(0).getSnippet().getTitle())
                .channelThumbnail(channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl())
                .uploadsList(channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads())
                .subscribers(channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue())
                .bannerExternalUrl(channelListResponse.getItems().get(0).getBrandingSettings().isEmpty() ?
                        null : channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl())
                .build();

        return channelsRepository.save(channels).getChannelId();
    }

    private String updateChannels(ChannelListResponse channelListResponse, Channels channels) {
        channels.update(channelListResponse.getItems().get(0).getSnippet().getTitle(),
                channelListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl(),
                channelListResponse.getItems().get(0).getContentDetails().getRelatedPlaylists().getUploads(),
                channelListResponse.getItems().get(0).getStatistics().getSubscriberCount().intValue(),
                channelListResponse.getItems().get(0).getBrandingSettings().getImage().getBannerExternalUrl());
        return channels.getChannelId();
    }

    private String saveVideos(VideoListResponse videoListResponse) {
        Channels channels = channelsRepository.findByChannelId(videoListResponse.getItems().get(0).getSnippet().getChannelId());
        if (channels == null)
            throw new IllegalArgumentException("Channel is null = " + videoListResponse.getItems().get(0).getSnippet().getChannelId());

        Videos videos = Videos.builder()
                .channels(channels)
                .videoId(videoListResponse.getItems().get(0).getId())
                .videoName(videoListResponse.getItems().get(0).getSnippet().getTitle())
                .videoThumbnail(videoListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl())
                .videoDescription(videoListResponse.getItems().get(0).getSnippet().getDescription())
                .videoPublishedDate(ConvertUtcDateTime.StringToUTCDateTime(videoListResponse.getItems().get(0).getSnippet().getPublishedAt()))
                .videoDuration(videoListResponse.getItems().get(0).getContentDetails().getDuration())
                .videoEmbeddable(videoListResponse.getItems().get(0).getStatus().getEmbeddable())
                .viewCount(videoListResponse.getItems().get(0).getStatistics().getViewCount().intValue())
                .likeCount(videoListResponse.getItems().get(0).getStatistics().getLikeCount().intValue())
                .dislikeCount(videoListResponse.getItems().get(0).getStatistics().getDislikeCount().intValue())
                .commentCount(videoListResponse.getItems().get(0).getStatistics().getCommentCount().intValue())
                .tags(videoListResponse.getItems().get(0).getSnippet().getTags())
                .build();

        return videosRepository.save(videos).getVideoId();
    }

    private String updateVideos(VideoListResponse videoListResponse, Videos videos) {
        videos.update(videoListResponse.getItems().get(0).getSnippet().getTitle(),
                videoListResponse.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl(),
                videoListResponse.getItems().get(0).getSnippet().getDescription(),
                ConvertUtcDateTime.StringToUTCDateTime(videoListResponse.getItems().get(0).getSnippet().getPublishedAt()),
                videoListResponse.getItems().get(0).getContentDetails().getDuration(),
                videoListResponse.getItems().get(0).getStatus().getEmbeddable(),
                videoListResponse.getItems().get(0).getStatistics().getViewCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getLikeCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getDislikeCount().intValue(),
                videoListResponse.getItems().get(0).getStatistics().getCommentCount().intValue(),
                videoListResponse.getItems().get(0).getSnippet().getTags());
        return videos.getVideoId();
    }

}
