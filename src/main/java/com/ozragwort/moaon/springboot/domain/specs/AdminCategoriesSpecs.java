package com.ozragwort.moaon.springboot.domain.specs;

import com.ozragwort.moaon.springboot.domain.categories.Categories;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminCategoriesSpecs {

    public enum SearchKey {
        ID("idx"),
        PAGE(""),SIZE(""),SORT("");

        private final String value;

        SearchKey(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static List<Predicate> getPredicateByKeyword(Map<SearchKey, Object> keyword, Root<Categories> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicate = new ArrayList<>();

        for (SearchKey key : keyword.keySet()) {
            if (key == SearchKey.ID) {
                predicate.add(criteriaBuilder.equal(
                        root.get(key.value), keyword.get(key)
                ));
            }
        }

        return predicate;
    }

}
