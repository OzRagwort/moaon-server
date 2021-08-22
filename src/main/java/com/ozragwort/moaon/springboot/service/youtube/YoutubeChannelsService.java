package com.ozragwort.moaon.springboot.service.youtube;

import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.channels.ChannelsRepository;
import com.ozragwort.moaon.springboot.domain.specs.AdminChannelsSpecs;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.videos.*;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import com.ozragwort.moaon.springboot.util.youtube.YoutubeDataApi;
import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.PageRequest;
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

import static com.ozragwort.moaon.springboot.domain.specs.AdminChannelsSpecs.getPredicateByKeyword;
import static com.ozragwort.moaon.springboot.domain.specs.AdminChannelsSpecs.SearchKey;

@RequiredArgsConstructor
@Service
public class YoutubeChannelsService {

    private final EntityManagerFactory entityManagerFactory;
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
    public List<ChannelsResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        EntityManager em = sessionFactory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        CriteriaQuery<Channels> criteriaQuery = builder.createQuery(Channels.class);

        Root<Channels> root = criteriaQuery.from(Channels.class);
        Map<AdminChannelsSpecs.SearchKey, Object> specKeys = makeSpeckKey(keyword);
        List<Predicate> snippetPredicate = new ArrayList<>(getPredicateByKeyword
                (specKeys, root, criteriaQuery, builder));

        criteriaQuery
                .select(root)
                .where(snippetPredicate.toArray(new Predicate[0]))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        TypedQuery<Channels> query = em
                .createQuery(criteriaQuery)
                .setFirstResult(page * size)
                .setMaxResults(size);

        return query.getResultList().stream()
                .map(ChannelsResponseDto::new)
                .collect(Collectors.toList());
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
