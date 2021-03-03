package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    private final VideosRepository videosRepository;

    @PersistenceUnit
    EntityManagerFactory entityManagerFactory;

    public List<VideosResponseDto> searchVideosByKeywords(String keyword, String categoryId, int page, int size) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Long> categoryList = StringToListCategories(categoryId);

        entityManager.getTransaction().begin();

        String queryStr = "SELECT p FROM Videos p WHERE " +
                "p.channels.categories.idx IN (?1) and " +
                "(matchs_natural(p.videoName, p.videoDescription, '"+keyword+"') > 0 or matchs_boolean(p.videoName, p.videoDescription, '"+keyword+"') > 0) " +
                "ORDER BY p.score DESC";

        Query query = entityManager.createQuery(queryStr, Videos.class)
                .setParameter(1, categoryList);

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
    public List<VideosResponseDto> searchVideosByTags(String keywords, String categoryId, boolean random, Pageable pageable) {
        List<Videos> videosList;
        List<Long> categoryList = StringToListCategories(categoryId);
        if (random) {
            videosList = videosRepository.findRandTagsByKeyword(keywords, categoryList, pageable);
        } else {
            videosList = videosRepository.findTagsByKeyword(keywords, categoryList, pageable);
        }
        return videosList.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
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
