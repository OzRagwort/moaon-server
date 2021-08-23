package com.ozragwort.moaon.springboot.service.categories;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.dto.categories.CategoriesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.ozragwort.moaon.springboot.domain.specs.CategoriesSpecs.searchWith;
import static com.ozragwort.moaon.springboot.domain.specs.CategoriesSpecs.SearchKey;

@RequiredArgsConstructor
@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Transactional
    public CategoriesResponseDto findById(Long idx) {
        Categories categories = categoriesRepository.findById(idx)
                .orElse(null);

        return categories == null
                ? null
                : new CategoriesResponseDto(categories);
    }

    @Transactional
    public List<CategoriesResponseDto> findAll(Map<String, Object> keyword, Pageable pageable) {
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

        Specification<Categories> categoriesSpecification = searchWith(searchKeyword);

        return categoriesRepository.findAll(categoriesSpecification, pageable).stream()
                .map(CategoriesResponseDto::new)
                .collect(Collectors.toList());
    }

}
