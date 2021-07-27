package com.ozragwort.moaon.springboot.service.videos;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.specs.VideosSnippetSpecs;
import com.ozragwort.moaon.springboot.domain.specs.VideosStatisticsSpecs;
import com.ozragwort.moaon.springboot.domain.videos.*;
import com.ozragwort.moaon.springboot.dto.videos.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.ozragwort.moaon.springboot.domain.specs.VideosSnippetSpecs.SnippetSearchKey;
import static com.ozragwort.moaon.springboot.domain.specs.VideosStatisticsSpecs.StatisticsSearchKey;
import static com.ozragwort.moaon.springboot.util.Calculation.calcScore;
import static com.ozragwort.moaon.springboot.util.ConvertTo.DurationStringToSecond;
import static com.ozragwort.moaon.springboot.util.ConvertTo.StringToUTCDateTime;
import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class VideosService {

    private final EntityManagerFactory entityManagerFactory;
    private final VideosSnippetRepository videosSnippetRepository;
    private final VideosStatisticsRepository videosStatisticsRepository;
    private final ChannelsRepository channelsRepository;
    private final CategoriesRepository categoriesRepository;

    @Transactional
    public VideosResponseDto save(VideosSaveRequestDto requestDto) {
        checkArgument(
                isNull(
                        videosSnippetRepository.findByVideoId(requestDto.getSnippet().getVideosId()).orElse(null)
                ), "Already saved. Video ID : " + requestDto.getSnippet().getVideosId()
        );

        VideosSnippetSaveRequestDto snippetDto = requestDto.getSnippet();
        VideosStatisticsSaveRequestDto statisticsDto = requestDto.getStatistics();

        Channels channels = channelsRepository.findByChannelId(snippetDto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("No Channel found. Channel ID : " + snippetDto.getChannelId()));

        VideosSnippet savedVideoSnippet = videosSnippetRepository.save(snippetDto.toEntity(channels));
        VideosStatistics savedVideosStatistics = videosStatisticsRepository.save(statisticsDto.toEntity(savedVideoSnippet));

        return new VideosResponseDto(savedVideoSnippet, savedVideosStatistics);
    }

    @Transactional
    public VideosResponseDto update(String videoId, VideosUpdateRequestDto requestDto) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));
        VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        VideosSnippetUpdateRequestDto snippetDto = requestDto.getSnippet();
        VideosStatisticsUpdateRequestDto statisticsDto = requestDto.getStatistics();

        videosSnippet.update(
                snippetDto.getVideosName(),
                snippetDto.getVideosThumbnail(),
                snippetDto.getVideosDescription(),
                StringToUTCDateTime(snippetDto.getVideosPublishedDate()),
                DurationStringToSecond(snippetDto.getVideosDuration()),
                snippetDto.getTags()
        );

        double score = calcScore(statisticsDto.getViewCount(),
                statisticsDto.getLikeCount(),
                statisticsDto.getDislikeCount(),
                statisticsDto.getCommentCount());
        videosStatistics.update(
                statisticsDto.getViewCount(),
                statisticsDto.getLikeCount(),
                statisticsDto.getDislikeCount(),
                statisticsDto.getCommentCount(),
                score
        );

        return new VideosResponseDto(videosSnippet, videosStatistics);
    }

    @Transactional
    public VideosSnippetResponseDto findById(Long idx) {
        VideosSnippet videosSnippet = videosSnippetRepository.findById(idx)
                .orElse(null);

        return videosSnippet == null
                ? null
                : new VideosSnippetResponseDto(videosSnippet);
    }

    @Transactional
    public VideosSnippetResponseDto findByVideoId(String videoId) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElse(null);

        return videosSnippet == null
                ? null
                : new VideosSnippetResponseDto(videosSnippet);
    }

    @Transactional
    public VideosStatisticsResponseDto findStatisticsById(String videoId) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));
        VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        return new VideosStatisticsResponseDto(videosStatistics);
    }

    @Transactional
    public List<VideosSnippetResponseDto> findAll(Pageable pageable) {
        return videosSnippetRepository.findAll(pageable).stream()
                .map(VideosSnippetResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosSnippetResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        // 기본 설정
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        EntityManager em = sessionFactory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        // createQuery
        CriteriaQuery<VideosSnippet> snippetCriteriaQuery = builder.createQuery(VideosSnippet.class);
        CriteriaQuery<VideosStatistics> statisticsCriteriaQuery = builder.createQuery(VideosStatistics.class);

        // snippet
        Root<VideosSnippet> snippetRoot = snippetCriteriaQuery.from(VideosSnippet.class);
        Map<SnippetSearchKey, Object> snippetKeyword = makeSnippetSpecKey(keyword);
        List<Predicate> snippetPredicate = new ArrayList<>(VideosSnippetSpecs.getPredicateByKeyword
                (snippetKeyword, snippetRoot, snippetCriteriaQuery, builder));

        // statistics 조건이 있는 경우
        if (keyword.containsKey("score")) {
            Root<VideosStatistics> statisticsRoot = statisticsCriteriaQuery.from(VideosStatistics.class);
            Map<StatisticsSearchKey, Object> statisticsKeyword = makeStatisticsSpecKey(keyword);
            Predicate[] statisticsPredicate = VideosStatisticsSpecs.getPredicateByKeyword
                    (statisticsKeyword, statisticsRoot, statisticsCriteriaQuery, builder)
                    .toArray(new Predicate[0]);
            statisticsCriteriaQuery.where(statisticsPredicate);

            TypedQuery<VideosStatistics> statisticsTypedQuery = em
                    .createQuery(statisticsCriteriaQuery)
                    .setFirstResult(page * size)
                    .setMaxResults(size);
            List<Long> idxs = statisticsTypedQuery.getResultList()
                    .stream().map(VideosStatistics::getIdx).collect(Collectors.toList());
            snippetPredicate.add(builder.in(snippetRoot.get("idx")).value(idxs));
        }

        // 쿼리 실행
        snippetCriteriaQuery
                .select(snippetRoot)
                .where(snippetPredicate.toArray(new Predicate[0]))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), snippetRoot, builder));
        TypedQuery<VideosSnippet> query = em
                .createQuery(snippetCriteriaQuery)
                .setFirstResult(page * size)
                .setMaxResults(size);

        return query.getResultList().stream()
                .map(VideosSnippetResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<VideosSnippetResponseDto> findAllByChannelsId(String channelId, Map<String, Object> keyword, Pageable pageable) {
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
    public List<VideosSnippetResponseDto> findAllByCategoriesId(Long categoriesId, Map<String, Object> keyword, Pageable pageable) {
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
    public void delete(String videoId) {
        VideosSnippet videosSnippet = videosSnippetRepository.findByVideoId(videoId)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));
        VideosStatistics videosStatistics = videosStatisticsRepository.findByVideosSnippet(videosSnippet)
                .orElseThrow(() -> new NoSuchElementException("No Videos found. Video ID : " + videoId));

        videosStatisticsRepository.delete(videosStatistics);
        videosSnippetRepository.delete(videosSnippet);
    }

    private Map<SnippetSearchKey, Object> makeSnippetSpecKey(Map<String, Object> keyword) {
        Map<SnippetSearchKey, Object> searchKeyword = new HashMap<>();

        List<String> searchKeys = new ArrayList<String>(){{
            this.addAll(
                    Arrays.stream(SnippetSearchKey.values()).map(SnippetSearchKey::toString).collect(Collectors.toList())
            );
        }};

        for (String key : keyword.keySet()) {
            if (searchKeys.contains(key.toUpperCase())) {
                searchKeyword.put(SnippetSearchKey.valueOf(key.toUpperCase()), keyword.get(key));
            }
        }

        if (searchKeyword.containsKey(SnippetSearchKey.CHANNELID)) {
            Channels channels = channelsRepository.findByChannelId((String) searchKeyword.get(SnippetSearchKey.CHANNELID))
                    .orElse(null);
            searchKeyword.put(SnippetSearchKey.valueOf("CHANNELID"), channels);
        }
        if (searchKeyword.containsKey(SnippetSearchKey.CATEGORYID)) {
            Categories categories = categoriesRepository.findById((Long) searchKeyword.get(SnippetSearchKey.CATEGORYID))
                    .orElse(null);
            searchKeyword.put(SnippetSearchKey.valueOf("CATEGORYID"), categories);
        }
        if (searchKeyword.containsKey(SnippetSearchKey.TAGS)) {
            List<String> tags = new ArrayList<String>(){{add((String) searchKeyword.get(SnippetSearchKey.TAGS));}};
            searchKeyword.put(SnippetSearchKey.TAGS, tags);
        }

        return searchKeyword;
    }

    private Map<StatisticsSearchKey, Object> makeStatisticsSpecKey(Map<String, Object> keyword) {
        Map<StatisticsSearchKey, Object> searchKeyword = new HashMap<>();

        List<String> searchKeys = new ArrayList<String>(){{
            this.addAll(
                    Arrays.stream(StatisticsSearchKey.values()).map(StatisticsSearchKey::toString).collect(Collectors.toList())
            );
        }};

        for (String key : keyword.keySet()) {
            if (searchKeys.contains(key.toUpperCase())) {
                searchKeyword.put(StatisticsSearchKey.valueOf(key.toUpperCase()), keyword.get(key));
            }
        }

        return searchKeyword;
    }

}
