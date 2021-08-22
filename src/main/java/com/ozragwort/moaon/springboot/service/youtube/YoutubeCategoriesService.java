package com.ozragwort.moaon.springboot.service.youtube;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.dto.categories.CategoriesResponseDto;
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

import static com.ozragwort.moaon.springboot.domain.specs.AdminCategoriesSpecs.SearchKey;
import static com.ozragwort.moaon.springboot.domain.specs.AdminCategoriesSpecs.getPredicateByKeyword;

@RequiredArgsConstructor
@Service
public class YoutubeCategoriesService {

    private final EntityManagerFactory entityManagerFactory;

    @Transactional
    public List<CategoriesResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        EntityManager em = sessionFactory.createEntityManager();
        CriteriaBuilder builder = em.getCriteriaBuilder();
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        CriteriaQuery<Categories> criteriaQuery = builder.createQuery(Categories.class);

        Root<Categories> root = criteriaQuery.from(Categories.class);
        Map<SearchKey, Object> specKeys = makeSpeckKey(keyword);
        List<Predicate> snippetPredicate = new ArrayList<>(getPredicateByKeyword
                (specKeys, root, criteriaQuery, builder));

        criteriaQuery
                .select(root)
                .where(snippetPredicate.toArray(new Predicate[0]))
                .orderBy(QueryUtils.toOrders(pageable.getSort(), root, builder));
        TypedQuery<Categories> query = em
                .createQuery(criteriaQuery)
                .setFirstResult(page * size)
                .setMaxResults(size);

        return query.getResultList().stream()
                .map(CategoriesResponseDto::new)
                .collect(Collectors.toList());
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

}
