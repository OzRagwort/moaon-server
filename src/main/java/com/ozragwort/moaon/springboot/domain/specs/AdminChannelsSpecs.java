package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.channels.Channels;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminChannelsSpecs {

    public enum SearchKey {
        NO("idx"),
        ID("channelId"),
        CATEGORYID("categories"),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Predicate> getPredicateByKeyword(Map<SearchKey, Object> keyword, Root<Channels> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (SearchKey key : keyword.keySet()) {
            switch (key) {
                case NO:
                case ID:
                case CATEGORYID:
                    predicate.add(criteriaBuilder.equal(
                            root.get(key.value), keyword.get(key)
                    ));
                    break;
            }
        }

        return predicate;
    }

}
