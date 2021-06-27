package com.ozragwort.moaon.springboot.v1.service;

import com.ozragwort.moaon.springboot.v1.domain.categories.Categories;
import com.ozragwort.moaon.springboot.v1.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Transactional
    public Long save(CategoriesSaveRequestDto categoriesSaveRequestDto) {
        return categoriesRepository.save(categoriesSaveRequestDto.toEntity()).getIdx();
    }

    @Transactional
    public Long update(Long idx, CategoriesUpdateRequestDto categoriesUpdateRequestDto) {
        Categories categories = categoriesRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        categories.update(categoriesUpdateRequestDto.getCategoryName());

        return idx;
    }

    @Transactional
    public CategoriesResponseDto findById(Long idx) {
        Categories categories = categoriesRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));

        return new CategoriesResponseDto(categories);
    }

    @Transactional
    public List<CategoriesResponseDto> findAll() {
        return categoriesRepository.findAll().stream()
                .map(CategoriesResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CategoriesResponseDto> findAll(Pageable pageable) {
        return categoriesRepository.findAll(pageable).stream()
                .map(CategoriesResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long delete(Long idx) {
        Categories categories = categoriesRepository.findById(idx)
                .orElseThrow(() -> new IllegalArgumentException("id가 없음. id=" + idx));
        categoriesRepository.delete(categories);

        return idx;
    }

}
