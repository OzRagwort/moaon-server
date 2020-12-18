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
import org.springframework.data.domain.PageRequest;
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

    // 전체 인덱싱
    // com..component.StartupApplication.onApplicationEvent

    // 영상 검색
    public List<VideosResponseDto> searchVideos(String keyword) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Videos.class)
                .get();
        Query query = queryBuilder.keyword().wildcard()
                .onFields("videoName").boostedTo(2f)
                .andField("videoDescription")
                .matching("*" + keyword + "*")
                .createQuery();
        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Videos.class);
        List<Videos> videosResponse = (List<Videos>) fullTextQuery.getResultList();

        return videosResponse.stream()
                .map(VideosResponseDto::new)
                .collect(Collectors.toList());
    }

}
