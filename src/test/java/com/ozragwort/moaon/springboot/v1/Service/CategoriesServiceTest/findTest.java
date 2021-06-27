package com.ozragwort.moaon.springboot.v1.Service.CategoriesServiceTest;

import com.ozragwort.moaon.springboot.v1.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.v1.service.CategoriesService;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.v1.web.dto.CategoriesSaveRequestDto;
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
        Long idx = categoriesService.save(categoriesSaveRequestDto);
        categoriesResponseDto = categoriesService.findById(idx);

        //then
        assertThat(categoriesRepository.findById(idx)).isPresent();
        assertThat(categoriesRepository.findById(idx).get().getCategoryName()).isEqualTo(categoriesResponseDto.getCategoryName());
    }

}
