package com.ozragwort.moaon.springboot.domain.specs;

import com.google.common.base.Predicates;
import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import com.ozragwort.moaon.springboot.domain.videos.Videos;
import org.hibernate.query.criteria.internal.predicate.InPredicate;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
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
                    String str = (String) keyword.get(key);
                    List<String> ids = Arrays.asList(str.split(","));
                    predicate.add(criteriaBuilder.in(root.get(key.value)).value(ids));
                    break;
                case CHANNELID:
                    List<Channels> channelsList = (List<Channels>) keyword.get(key);
                    predicate.add(criteriaBuilder.in(root.get(key.value)).value(channelsList));
                    break;
                case CATEGORYID:
                    List<Categories> categoriesList = (List<Categories>) keyword.get(key);
                    predicate.add(criteriaBuilder.in(root.get("channels").get(key.value)).value(categoriesList));
                    break;
                case SEARCH:
                    String[] keywords = ((String) keyword.get(key)).split(" ");
                    Predicate predicates = criteriaBuilder.equal(criteriaBuilder.literal(0),1);

                    for (String searchKey : keywords) {
                        String value = "%" + searchKey + "%";
                        Predicate likeName = criteriaBuilder.like(root.get("videoName"), value);
                        Predicate likeDescription = criteriaBuilder.like(root.get("videoDescription"), value);
                        Predicate like = criteriaBuilder.or(likeName, likeDescription);
                        predicates = criteriaBuilder.or(like, predicates);
                    }

                    predicate.add(predicates);
                    break;
                case TAGS:
                    predicate.add(
                            criteriaBuilder.isMember(keyword.get(key), root.get(key.value))
                    );
                    break;
                case SHORTS:
                    Predicate shorts = criteriaBuilder.or(
                            criteriaBuilder.like(root.get("videoName"), "%Shorts%"),
                            criteriaBuilder.like(root.get("videoName"), "%shorts%"),
                            criteriaBuilder.like(root.get("videoDescription"), "%Shorts%"),
                            criteriaBuilder.like(root.get("videoDescription"), "%shorts%")
                    );
                    Predicate second = criteriaBuilder.lessThanOrEqualTo(root.get("videoDuration"), 60);

                    predicate.add(
                            criteriaBuilder.and(shorts, second)
                    );
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
