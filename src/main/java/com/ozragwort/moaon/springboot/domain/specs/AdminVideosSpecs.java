package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminVideosSpecs {

    public enum SearchKey {
        VIDEOID("videoId"),
        CHANNELID("channels"),
        CATEGORYID("categories"),
        SEARCH(""),
        TAGS("tags"),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Predicate> getPredicateByKeyword(Map<SearchKey, Object> keyword, Root<VideosSnippet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (SearchKey key : keyword.keySet()) {
            switch (key) {
                case VIDEOID:
                case CHANNELID:
                    predicate.add(criteriaBuilder.equal(
                            root.get(key.value), keyword.get(key)
                    ));
                    break;
                case CATEGORYID:
                    predicate.add(criteriaBuilder.equal(
                            root.get("channels").get(key.value), keyword.get(key)
                    ));
                    break;
                case SEARCH:
                    Predicate likeName = criteriaBuilder.like(root.get("videoName"), "*" + keyword.get(key) + "*");
                    Predicate likeDescription = criteriaBuilder.like(root.get("videoDescription"), "*" + keyword.get(key) + "*");

                    predicate.add(
                            criteriaBuilder.and(likeName, likeDescription)
                    );
                    break;
                case TAGS:
                    predicate.add(
                            criteriaBuilder.isMember(keyword.get(key), root.get(key.value))
                    );
                    break;
            }
        }

        return predicate;
    }

}
