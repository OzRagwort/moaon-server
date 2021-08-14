package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.videos.Videos;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideosSpecs {

    public enum VideosSearchKey {
        VIDEOID("videoId"),
        CHANNELID("channels"),
        CATEGORYID("categories"),
        SEARCH(""),
        TAGS("tags"),
        SHORTS(""),
        SCORE("score"),
        RANDOM(""),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        VideosSearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Videos> searchWith(Map<VideosSearchKey, Object> keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicate = getPredicateByKeyword(keyword, root, query, criteriaBuilder);
            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        };
    }

    public static List<Predicate> getPredicateByKeyword(Map<VideosSearchKey, Object> keyword, Root<Videos> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (VideosSearchKey key : keyword.keySet()) {
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
