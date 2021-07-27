package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.videos.VideosSnippet;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideosSnippetSpecs {

    public enum SnippetSearchKey {
        VIDEOID("videoId"),
        CHANNELID("channels"),
        CATEGORYID("categories"),
        SEARCH(""),
        TAGS("tags"),
        SHORTS(""),
        RANDOM(""),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        SnippetSearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Predicate> getPredicateByKeyword(Map<SnippetSearchKey, Object> keyword, Root<VideosSnippet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (SnippetSearchKey key : keyword.keySet()) {
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
                case SHORTS:
                    Predicate shortsName = criteriaBuilder.like(root.get("videoName"), "*#Shorts*");
                    Predicate shortsDescription = criteriaBuilder.like(root.get("videoDescription"), "*#Shorts*");

                    predicate.add(criteriaBuilder.and(shortsName, shortsDescription));
                    predicate.add(criteriaBuilder.lessThanOrEqualTo(root.get("videoDuration"), 60));
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
