package com.ozragwort.moaon.springboot.service.youtube;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.dto.admin.AdminSecretKeyDto;
import com.ozragwort.moaon.springboot.dto.admin.AdminVideosSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.util.youtube.YoutubeDataApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;
import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;

@RequiredArgsConstructor
@Service
public class YoutubeVideosService {

    private final ChannelsRepository channelsRepository;
    private final VideosRepository videosRepository;
    private final YoutubeDataApi youtubeDataApi;

    @Transactional
    public VideosResponseDto save(AdminVideosSaveRequestDto requestDto) {
        VideoListResponse videoListResponse = youtubeDataApi.getVideoListResponse(requestDto.getVideoId(), null);

        if (videoListResponse.getItems().isEmpty()) {
            return null;
        } else {
            Videos videos = videosRepository.save(responseToVideos(videoListResponse));

            return new VideosResponseDto(videos);
        }
    }

    @Transactional
    public VideosResponseDto refresh(String videoId, AdminSecretKeyDto secret) {
        Videos videos = videosRepository.findByVideoId(videoId)
                .orElse(new Videos());

        String key = secret == null || secret.getSecret().length() == 0
                ? null
                : secret.getSecret();
        VideoListResponse VideoListResponse = youtubeDataApi.getVideoListResponse(videoId, key);

        if (VideoListResponse == null) {
            return new VideosResponseDto(videos);
        } else {
            return new VideosResponseDto(updateVideos(VideoListResponse, videos));
        }

    }

    @Transactional
    public void delete(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        videosRepository.delete(videos);
    }


    private Videos responseToVideos(VideoListResponse videoListResponse) {
        Video item = videoListResponse.getItems().get(0);
        VideoSnippet snippet = item.getSnippet();
        VideoStatistics statistics = item.getStatistics();

        Channels channels = channelsRepository.findByChannelId(item.getSnippet().getChannelId())
                .orElseThrow(() -> new NoSuchElementException("No Channels found. Channel ID : " + item.getSnippet().getChannelId()));

        String videoId = item.getId();
        String videoName = snippet.getTitle();
        String videoDescription = snippet.getDescription();
        String videoThumbnail = snippet.getThumbnails().getMedium().getUrl();
        LocalDateTime videoPublishedDate = StringToUTCDateTime(snippet.getPublishedAt());
        long videoDuration = DurationStringToSecond(item.getContentDetails().getDuration());
        int viveCount = statistics.getViewCount().intValue();
        int likeCount = statistics.getLikeCount().intValue();
        int dislikeCount = statistics.getDislikeCount().intValue();
        int commentCount = statistics.getCommentCount().intValue();
        double score = calcScore(viveCount, likeCount, dislikeCount, commentCount);
        List<String> tags = snippet.getTags();

        return Videos.builder()
                .channels(channels)
                .videoId(videoId)
                .videoName(videoName)
                .videoDescription(videoDescription)
                .videoThumbnail(videoThumbnail)
                .videoPublishedDate(videoPublishedDate)
                .videoDuration(videoDuration)
                .viewCount(viveCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .score(score)
                .tags(tags)
                .build();
    }

    private Videos updateVideos(VideoListResponse videoListResponse, Videos videos) {
        Videos response = responseToVideos(videoListResponse);

        videos.update(
                response.getVideoName(),
                response.getVideoThumbnail(),
                response.getVideoDescription(),
                response.getVideoPublishedDate(),
                response.getVideoDuration(),
                response.getViewCount(),
                response.getLikeCount(),
                response.getDislikeCount(),
                response.getCommentCount(),
                calcScore(
                        response.getViewCount(),
                        response.getLikeCount(),
                        response.getDislikeCount(),
                        response.getCommentCount()
                ),
                response.getTags()
        );

        return videos;
    }

}
