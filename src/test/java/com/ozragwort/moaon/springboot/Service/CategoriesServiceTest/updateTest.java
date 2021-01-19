package com.ozragwort.moaon.springboot.Service.CategoriesServiceTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.service.CategoriesService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.CategoriesUpdateRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class updateTest {

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CategoriesRepository categoriesRepository;

    @After
    public void cleanup() {
        categoriesRepository.deleteAll();
    }

    @Test
    public void Categories_Service_update_Test() {
        //given
        Long idx;
        String categoryName = "categoryName";
        String categoryNameUpdate = "categoryNameUpdate";

        CategoriesSaveRequestDto categoriesSaveRequestDto = CategoriesSaveRequestDto.builder()
                .categoryName(categoryName)
                .build();

        //when
        idx = categoriesService.save(categoriesSaveRequestDto);
        categoriesService.update(idx, new CategoriesUpdateRequestDto(categoryNameUpdate));

        //then
        assertThat(categoriesRepository.findById(idx)).isPresent();
        assertThat(categoriesRepository.findById(idx).get().getCategoryName()).isEqualTo(categoryNameUpdate);
    }

}
