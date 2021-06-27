package com.ozragwort.moaon.springboot.v1.web.dto;

import com.ozragwort.moaon.springboot.v1.domain.categories.Categories;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoriesSaveRequestDto {

    private String categoryName;

    @Builder
    public CategoriesSaveRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

    public Categories toEntity() {
        return Categories.builder()
                .categoryName(categoryName)
                .build();
    }

}
