package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.channels.Channels;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ChannelsSpecs {

    public enum SearchKey {
        ID("idx"),
        CATEGORYID("categories"),
        CHANNELID("channelId"),
        CHANNELNAME("channelName"),
        SUBSCRIBEROVER("subscribers"),
        SUBSCRIBERUNDER("subscribers"),
        RANDOM(""),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static Specification<Channels> searchWith(Map<SearchKey, Object> keyword) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicate = getPredicateByKeyword(keyword, root, query, criteriaBuilder);
            return criteriaBuilder.and(predicate.toArray(new Predicate[0]));
        };
    }

    private static List<Predicate> getPredicateByKeyword(Map<SearchKey, Object> keyword, Root<Channels> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (SearchKey key : keyword.keySet()) {
            switch (key) {
                case ID:
                    predicate.add(criteriaBuilder.equal(
                            root.get(key.value), keyword.get(key)
                    ));
                    break;
                case CHANNELID:
                    String str = (String) keyword.get(key);
                    List<String> ids = Arrays.asList(str.split(","));
                    predicate.add(criteriaBuilder.in(root.get(key.value)).value(ids));
                    break;
                case CATEGORYID:
                    List<Categories> categoriesList = (List<Categories>) keyword.get(key);
                    predicate.add(criteriaBuilder.in(root.get(key.value)).value(categoriesList));
                    break;
                case CHANNELNAME:
                    predicate.add(criteriaBuilder.like(
                            root.get(key.value), "*" + keyword.get(key) + "*"
                    ));
                    break;
                case SUBSCRIBEROVER:
                    int subscriberOver = Integer.parseInt(keyword.get(key).toString());
                    predicate.add(criteriaBuilder.greaterThanOrEqualTo(
                            root.get(key.value), subscriberOver
                    ));
                    break;
                case SUBSCRIBERUNDER:
                    int subscriberUnder = Integer.parseInt(keyword.get(key).toString());
                    predicate.add(criteriaBuilder.lessThanOrEqualTo(
                            root.get(key.value), subscriberUnder
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
