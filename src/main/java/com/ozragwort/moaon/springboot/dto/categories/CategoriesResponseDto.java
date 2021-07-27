package com.ozragwort.moaon.springboot.dto.categories;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@NoArgsConstructor
public class CategoriesResponseDto {

    private Long idx;
    private String categoryName;

    public CategoriesResponseDto(Categories categories) {
        this.idx = categories.getIdx();
        this.categoryName = categories.getCategoryName();
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

}
