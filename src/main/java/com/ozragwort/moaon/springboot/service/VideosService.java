package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.component.CheckIdType;
import com.ozragwort.moaon.springboot.component.ConvertUtcDateTime;
import com.ozragwort.moaon.springboot.component.ScoreCalculation;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class VideosService {

    private final VideosRepository videosRepository;

    private final ChannelsRepository channelsRepository;

    private final CategoriesRepository categoriesRepository;

    private final ScoreCalculation scoreCalculation;

    @Transactional
    public String save(VideosSaveRequestDto requestDto) {

        if (videosRepository.findByVideoId(requestDto.getVideoId()) != null)
            throw new IllegalArgumentException("Video is not empty = " + requestDto.getVideoId());
        Channels channels = channelsRepository.findByChannelId(requestDto.getChannelId());
        if (channels == null)
            throw new IllegalArgumentException("no channel = " + requestDto.getChannelId());

        double score = scoreCalculation.makeScore(requestDto.getViewCount(),requestDto.getLikeCount(),requestDto.getDislikeCount(),requestDto.getCommentCount());

        Videos videos = Videos.builder()
                .videoDescription(requestDto.getVideoDescription())
                .channels(channels)
                .videoId(requestDto.getVideoId())
                .videoName(requestDto.getVideoName())
                .videoThumbnail(requestDto.getVideoThumbnail())
                .videoPublishedDate(ConvertUtcDateTime.StringToUTCDateTime(requestDto.getVideoPublishedDate()))
                .videoDuration(requestDto.getVideoDuration())
                .videoEmbeddable(requestDto.isVideoEmbeddable())
                .viewCount(requestDto.getViewCount())
                .likeCount(requestDto.getLikeCount())
                .dislikeCount(requestDto.getDislikeCount())
                .commentCount(requestDto.getCommentCount())
                .score(score)
                .tags(requestDto.getTags())
                .build();

        return videosRepository.save(videos).getVideoId();
    }

    @Transactional
    public String saveRelations(String videoId, RelatedVideosSaveRequestDto requestDto) {
        Videos videos = videosRepository.findByVideoId(videoId);
        videos.setRelatedVideos(requestDto.getRelatedVideo());

        return videos.getVideoId();
    }

    @Transactional
    public String update(String videoId, VideosUpdateRequestDto requestDto) {
        Videos videos = videosRepository.findByVideoId(videoId);

        double score = scoreCalculation.makeScore(requestDto.getViewCount(),requestDto.getLikeCount(),requestDto.getDislikeCount(),requestDto.getCommentCount());

        videos.update(requestDto.getVideoName(),
                requestDto.getVideoThumbnail(),
                requestDto.getVideoDescription(),
                ConvertUtcDateTime.StringToUTCDateTime(requestDto.getVideoPublishedDate()),
                requestDto.getVideoDuration(),
                requestDto.isVideoEmbeddable(),
                requestDto.getViewCount(),
                requestDto.getLikeCount(),
                requestDto.getDislikeCount(),
                requestDto.getCommentCount(),
                score,
                requestDto.getTags().stream().map(String::new).distinct().collect(Collectors.toList()));

        return videos.getVideoId();
    }

    @Transactional
    public String addRelations(String videoId, RelatedVideosUpdateRequestDto requestDto) {
        CheckIdType checkIdType = new CheckIdType();
        for (String relatedVideo : requestDto.getRelatedVideo()) {
            if (!checkIdType.checkVideoId(relatedVideo)) {
                return null;
            }
        }

        Videos videos = videosRepository.findByVideoId(videoId);
        videos.addRelations(requestDto.getRelatedVideo());
        return videos.getVideoId();
    }

    @Transactional
    public double getScoreAvgByChannelId(String channelId) {
        return videosRepository.getScoreAvgByChannelId(channelId);
    }

    @Transactional
    public List<String> getTagsByChannelId(String channelId, Pageable pageable) {
        Channels channels = channelsRepository.findByChannelId(channelId);
        if (channels == null) {
            throw new IllegalArgumentException("no channel = " + channelId);
        } else {
            return videosRepository.getTagsByChannelId(channelId, pageable);
        }
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
    public List<VideosResponseDto> findByChannelId(String channelId, boolean random, Pageable pageable) {
        List<Channels> channelsList = StringToListChannels(channelId);

        if (random) {
            return videosRepository.findRandByChannelId(channelsList, pageable).stream()
                    .map(VideosResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            return videosRepository.findByChannelId(channelsList, pageable).stream()
                    .map(VideosResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<VideosResponseDto> findByCategoryIdx(String categoryId, boolean random, Pageable pageable) {
        List<Long> categoryList = StringToListCategories(categoryId);
        if (random) {
            return videosRepository.findRandByCategoryIdx(categoriesRepository.findByIdxList(categoryList), pageable).stream()
                    .map(VideosResponseDto::new)
                    .collect(Collectors.toList());
        } else {
            return videosRepository.findByCategoryIdx(categoriesRepository.findByIdxList(categoryList), pageable).stream()
                    .map(VideosResponseDto::new)
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public List<VideosResponseDto> findByScore(double score, String categoryId, boolean random, Pageable pageable) {
        List<Videos> videos;
        List<Long> categoryList = StringToListCategories(categoryId);
        if (random) {
            videos = videosRepository.findRandByScore(score, categoryList, pageable);
        } else {
            videos = videosRepository.findByScore(score, categoryList, pageable);
        }
        return videos.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosResponseDto> findOverAvgRandByScore(String categoryId, Pageable pageable) {
        List<Long> categoryList = StringToListCategories(categoryId);
        double avg = videosRepository.getScoreAvg(categoryList);
        return videosRepository.findRandByScore(avg, categoryList, pageable).stream()
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
    public List<RelatedVideosResponseDto> findRelationsByVideoId(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId);
        List<String> relatedVideos = videos.getRelatedVideos().stream().map(String::new).collect(Collectors.toList());
        List<RelatedVideosResponseDto> relatedVideosResponseDtos = new ArrayList<>();

        for (String relatedVideo : relatedVideos) {
            Videos v = videosRepository.findByVideoId(relatedVideo);
            if (v != null && videos.getChannels().getCategories().getIdx().equals(v.getChannels().getCategories().getIdx())){
                relatedVideosResponseDtos.add(new RelatedVideosResponseDto(v));
            }
        }

        return relatedVideosResponseDtos;
    }

    @Transactional
    public List<VideosResponseDto> findByPublishedDate(Long publishedDate, String categoryId, boolean random, Pageable pageable) {
        LocalDateTime convertedTime = ConvertUtcDateTime.nowTimeUnderHour(publishedDate.intValue());
        List<Videos> videos;
        List<Long> categoryList = StringToListCategories(categoryId);
        if (random) {
            videos = videosRepository.findRandByPublishedDate(convertedTime, categoryList, pageable);
        } else {
            videos = videosRepository.findByPublishedDate(convertedTime, categoryList, pageable);
        }
        return videos.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteAll() {
        videosRepository.deleteAll();
    }

    @Transactional
    public String delete(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId);
        videosRepository.delete(videos);

        return videos.getVideoId();
    }

    @Transactional
    public String deleteRelations(String videoId) {
        Videos videos = videosRepository.findByVideoId(videoId);
        videos.getRelatedVideos().clear();

        return videos.getVideoId();
    }

    private List<Channels> StringToListChannels(String channelId) {
        List<Channels> list = new ArrayList<>();
        String[] arr = channelId.split(",");

        for (String s : arr) {
            list.add(channelsRepository.findByChannelId(s));
        }

        return list;
    }

    private List<Long> StringToListCategories(String categoryId) {
        List<Long> list = new ArrayList<>();
        String[] arr = categoryId.split(",");

        for (String s : arr) {
            list.add(Long.parseLong(s));
        }

        return list;
    }
}
