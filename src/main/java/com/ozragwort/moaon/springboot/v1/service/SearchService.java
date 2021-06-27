package com.ozragwort.moaon.springboot.v1.service;

import com.ozragwort.moaon.springboot.v1.domain.videos.Videos;
import com.ozragwort.moaon.springboot.v1.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.v1.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final VideosRepository videosRepository;

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    public List<VideosResponseDto> searchVideosByKeywords(String keyword, Long categoryId, int page, int size) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        String queryStr = "SELECT p FROM Videos p WHERE " +
                "p.channels.categories.idx = ?1 and " +
                "(matchs_natural(p.videoName, p.videoDescription, '"+keyword+"') > 0 or matchs_boolean(p.videoName, p.videoDescription, '"+keyword+"') > 0) " +
                "ORDER BY p.score DESC";

        Query query = entityManager.createQuery(queryStr, Videos.class)
                .setParameter(1, categoryId);

        query.setFirstResult(page);
        query.setMaxResults(size);
        List<Videos> videosResponse = query.getResultList();
        List<VideosResponseDto> list = videosResponse.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());

        entityManager.getTransaction().commit();
        entityManager.close();

        return list;
    }

    @Transactional
    public List<VideosResponseDto> searchVideosByTags(String keywords, Long categoryId, boolean random, Pageable pageable) {
        List<Videos> videosList;
        if (random) {
            videosList = videosRepository.findRandTagsByKeyword(keywords, categoryId, pageable);
        } else {
            videosList = videosRepository.findTagsByKeyword(keywords, categoryId, pageable);
        }
        return videosList.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

}
