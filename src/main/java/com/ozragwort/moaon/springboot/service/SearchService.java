package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.domain.videos.VideosRepository;
import com.ozragwort.moaon.springboot.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
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

    public List<VideosResponseDto> searchVideosByKeywords(String keyword, int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        entityManager.getTransaction().begin();

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Videos.class)
                .get();
        Query query = getWildcardQuery(queryBuilder, keyword);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Videos.class);
        fullTextQuery.setFirstResult(page);
        fullTextQuery.setMaxResults(size);
        List<Videos> videosResponse = fullTextQuery.getResultList();
        List<VideosResponseDto> list = videosResponse.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());

        entityManager.getTransaction().commit();
        entityManager.close();

        return list;
    }

    @Transactional
    public List<VideosResponseDto> searchVideosByTags(String keywords, Long categoryIdx, boolean random, Pageable pageable) {
        List<Videos> videosList;
        if (random) {
            videosList = videosRepository.findRandTagByKeyword(keywords, categoryIdx, pageable);
        } else {
            videosList = videosRepository.findTagByKeyword(keywords, categoryIdx, pageable);
        }
        return videosList.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

    // 딱 그 키워드와 같은 결과
    private Query getKeywordQuery(QueryBuilder queryBuilder, String keyword) {
        return queryBuilder.keyword()
                .onFields("videoName").boostedTo(2f)
                .andField("videoDescription")
                .matching(keyword)
                .createQuery();
    }

    // 조건 추가 (?, *)
    // ? => 뭐가 들어가야 할 지 모르겠는것 test인지 text인지 모를때 te?t (한글자)
    // * => ?가 여러글자
    private Query getWildcardQuery(QueryBuilder queryBuilder, String keyword) {
        return queryBuilder.keyword().wildcard()
                .onFields("videoName").boostedTo(2f)
                .andField("videoDescription")
                .matching("*" + keyword + "*")
                .createQuery();
    }

    // 유사한것들 찾기 (~, ~0.8) default = 0.5
    private Query getFuzzyQuery(QueryBuilder queryBuilder, String keyword) {
        return queryBuilder.keyword().fuzzy()
                .onFields("videoName").boostedTo(2f)
                .andField("videoDescription")
                .matching(keyword)
                .createQuery();
    }

}
