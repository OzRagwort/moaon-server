package com.ozragwort.moaon.springboot.service.categories;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.dto.categories.CategoriesResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CategoriesResponseDto> findAll() {
        List<Categories> categories = categoriesRepository.findAll();

        return categories.stream()
                .map(CategoriesResponseDto::new)
                .collect(Collectors.toList());
    }

}
