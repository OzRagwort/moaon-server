package com.ozragwort.moaon.springboot.service.youtube;

import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoStatistics;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;
import com.ozragwort.moaon.springboot.domain.videos.VideosSnippetRepository;
import com.ozragwort.moaon.springboot.domain.videos.VideosStatistics;
import com.ozragwort.moaon.springboot.domain.videos.VideosStatisticsRepository;
import com.ozragwort.moaon.springboot.dto.admin.AdminVideosSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.util.youtube.YoutubeDataApi;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

import static com.ozragwort.moaon.springboot.domain.specs.AdminVideosSpecs.getPredicateByKeyword;
import static com.ozragwort.moaon.springboot.domain.specs.AdminVideosSpecs.SearchKey;
import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;
import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;

@RequiredArgsConstructor
@Service
public class YoutubeVideosService {

    private final EntityManagerFactory entityManagerFactory;
    private final ChannelsRepository channelsRepository;
    private final VideosSnippetRepository videosSnippetRepository;
    private final VideosStatisticsRepository videosStatisticsRepository;
    private final YoutubeDataApi youtubeDataApi;

    @Transactional
    public VideosResponseDto save(AdminVideosSaveRequestDto requestDto) {
        VideoListResponse videoListResponse = youtubeDataApi.getVideoListResponse(requestDto.getVideoId(), null);

        if (videoListResponse.getItems().isEmpty()) {
            return null;
        } else {
            VideosSnippet videosSnippet = videosSnippetRepository.save(responseToSnippet(videoListResponse));
            VideosStatistics videosStatistics = videosStatisticsRepository.save(responseToStatistics(videoListResponse, videosSnippet));

            return new VideosResponseDto(videosSnippet, videosStatistics);
        }
    }

    @Transactional
    public VideosResponseDto refresh(String videoId) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));
        VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        VideoListResponse VideoListResponse = youtubeDataApi.getVideoListResponse(videoId, null);

        return updateVideos(VideoListResponse, videosSnippet, videosStatistics);
    }

    @Transactional
    public List<VideosResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        EntityManager em = sessionFactory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        CriteriaQuery<VideosSnippet> criteriaQuery = builder.createQuery(VideosSnippet.class);

        Root<VideosSnippet> root = criteriaQuery.from(VideosSnippet.class);
        Map<SearchKey, Object> specKeys = makeSpeckKey(keyword);
        List<Predicate> snippetPredicate = new ArrayList<>(getPredicateByKeyword
                (specKeys, root, criteriaQuery, builder));

        // 쿼리 실행
        criteriaQuery
                .select(root)
                .where(snippetPredicate.toArray(new Predicate[0]))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        TypedQuery<VideosSnippet> query = em
                .createQuery(criteriaQuery)
                .setFirstResult(page * size)
                .setMaxResults(size);

        return query.getResultList().stream()
                .map(videosSnippet -> {
                    VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                            .orElseThrow(() -> new IllegalArgumentException(""));
                    return new VideosResponseDto(videosSnippet, videosStatistics);
                }).collect(Collectors.toList());
    }

    @Transactional
    public void delete(String videoId) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));
        VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        videosStatisticsRepository.delete(videosStatistics);
        videosSnippetRepository.delete(videosSnippet);
    }


    private Map<SearchKey, Object> makeSpeckKey(Map<String, Object> keyword) {
        Map<SearchKey, Object> searchKeyword = new HashMap<>();

        List<String> searchKeys = new ArrayList<String>(){{
            this.addAll(
                    Arrays.stream(SearchKey.values()).map(SearchKey::toString).collect(Collectors.toList())
            );
        }};

        for (String key : keyword.keySet()) {
            if (searchKeys.contains(key.toUpperCase())) {
                searchKeyword.put(SearchKey.valueOf(key.toUpperCase()), keyword.get(key));
            }
        }

        return searchKeyword;
    }

    private VideosSnippet responseToSnippet(VideoListResponse videoListResponse) {
        Video item = videoListResponse.getItems().get(0);
        Channels channels = channelsRepository.findByChannelId(item.getSnippet().getChannelId())
                .orElseThrow(() -> new NoSuchElementException("No Channels found. Channel ID : " + item.getSnippet().getChannelId()));

        return VideosSnippet.builder()
                .channels(channels)
                .videoId(item.getId())
                .videoName(item.getSnippet().getTitle())
                .videoDescription(item.getSnippet().getDescription())
                .videoThumbnail(item.getSnippet().getThumbnails().getMedium().getUrl())
                .videoPublishedDate(
                        StringToUTCDateTime(item.getSnippet().getPublishedAt())
                )
                .videoDuration(
                        DurationStringToSecond(item.getContentDetails().getDuration())
                )
                .tags(item.getSnippet().getTags())
                .build();
    }

    private VideosStatistics responseToStatistics(VideoListResponse videoListResponse, VideosSnippet videosSnippet) {
        VideoStatistics youtubeData = videoListResponse.getItems().get(0).getStatistics();
        int viveCount = youtubeData.getViewCount().intValue();
        int likeCount = youtubeData.getLikeCount().intValue();
        int dislikeCount = youtubeData.getDislikeCount().intValue();
        int commentCount = youtubeData.getCommentCount().intValue();
        double score = calcScore(viveCount, likeCount, dislikeCount, commentCount);

        return VideosStatistics.builder()
                .videosSnippet(videosSnippet)
                .viewCount(viveCount)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .commentCount(commentCount)
                .score(score)
                .build();
    }

    private VideosResponseDto updateVideos(VideoListResponse videoListResponse, VideosSnippet videosSnippet, VideosStatistics videosStatistics) {
        VideosSnippet updateSnippet = responseToSnippet(videoListResponse);
        VideosStatistics updateStatistics = responseToStatistics(videoListResponse, videosSnippet);

        videosSnippet.update(
                updateSnippet.getVideoName(),
                updateSnippet.getVideoThumbnail(),
                updateSnippet.getVideoDescription(),
                updateSnippet.getVideoPublishedDate(),
                updateSnippet.getVideoDuration(),
                updateSnippet.getTags()
        );

        videosStatistics.update(
                updateStatistics.getViewCount(),
                updateStatistics.getLikeCount(),
                updateStatistics.getDislikeCount(),
                updateStatistics.getCommentCount(),
                calcScore(
                        updateStatistics.getViewCount(),
                        updateStatistics.getLikeCount(),
                        updateStatistics.getDislikeCount(),
                        updateStatistics.getCommentCount()
                )
        );

        return new VideosResponseDto(videosSnippet, videosStatistics);
    }

}
