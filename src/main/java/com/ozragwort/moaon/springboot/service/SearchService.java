package com.ozragwort.moaon.springboot.service;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import com.ozragwort.moaon.springboot.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.search.Query;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SearchService {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    public List<VideosResponseDto> searchVideos(String keyword, int page, int size) {
        return getVideos(keyword, page, size);
    }

    private List<VideosResponseDto> getVideos(String keyword, int page, int size) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Videos.class)
                .get();

        Query query = getWildcardQuery(queryBuilder, keyword);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Videos.class);
        fullTextQuery.setFirstResult(page);
        fullTextQuery.setMaxResults(size);
        List<Videos> videosResponse = (List<Videos>) fullTextQuery.getResultList();

        return videosResponse.stream()
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