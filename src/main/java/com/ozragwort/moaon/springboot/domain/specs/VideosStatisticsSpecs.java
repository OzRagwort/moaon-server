package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.videos.VideosStatistics;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideosStatisticsSpecs {

    public enum StatisticsSearchKey {
        SCORE("score"),
        RANDOM(""),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        StatisticsSearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Predicate> getPredicateByKeyword(Map<StatisticsSearchKey, Object> keyword, Root<VideosStatistics> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (StatisticsSearchKey key : keyword.keySet()) {
            switch (key) {
                case SCORE:
                    predicate.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get(key.value), Double.valueOf(keyword.get(key).toString())
                    ));
                    break;
                case RANDOM:
                    if (keyword.get(key).equals("true")) {
                        query.orderBy(criteriaBuilder.asc(criteriaBuilder.function("RAND", null)));
                    }
                    break;
            }
        }

        return predicate;
    }

}
