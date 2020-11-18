package com.ozragwort.moaon.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoriesUpdateRequestDto {

    private String categoryName;

    @Builder
    public CategoriesUpdateRequestDto(String categoryName) {
        this.categoryName = categoryName;
    }

}
