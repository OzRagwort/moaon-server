package com.ozragwort.moaon.springboot.v1.web.dto;

import com.ozragwort.moaon.springboot.v1.domain.categories.Categories;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoriesResponseDto {

    private Long idx;
    private String categoryName;

    public CategoriesResponseDto(Categories categories) {
        this.idx = categories.getIdx();
        this.categoryName = categories.getCategoryName();
    }

}
