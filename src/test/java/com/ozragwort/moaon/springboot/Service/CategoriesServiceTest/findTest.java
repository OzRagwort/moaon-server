package com.ozragwort.moaon.springboot.Service.CategoriesServiceTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.service.CategoriesService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class findTest {

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CategoriesRepository categoriesRepository;

    @After
    public void cleanup() {
        categoriesRepository.deleteAll();
    }

    @Test
    public void Categories_Service_findById_Test() {
        //given
        String categoryName = "categoryName";

        CategoriesSaveRequestDto categoriesSaveRequestDto = CategoriesSaveRequestDto.builder()
                .categoryName(categoryName)
                .build();

        CategoriesResponseDto categoriesResponseDto;

        //when
        Long id = categoriesService.save(categoriesSaveRequestDto);
        categoriesResponseDto = categoriesService.findById(id);

        //then
        Categories categories = categoriesRepository.findAll().get(0);
        assertThat(categories.getCategoryName()).isEqualTo(categoriesResponseDto.getCategoryName());
    }

}
