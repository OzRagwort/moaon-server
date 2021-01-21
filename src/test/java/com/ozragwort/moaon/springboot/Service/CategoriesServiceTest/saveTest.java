package com.ozragwort.moaon.springboot.Service.CategoriesServiceTest;

import com.ozragwort.moaon.springboot.domain.categories.Categories;
import com.ozragwort.moaon.springboot.domain.categories.CategoriesRepository;
import com.ozragwort.moaon.springboot.service.CategoriesService;
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
public class saveTest {

    @Autowired
    CategoriesService categoriesService;

    @Autowired
    CategoriesRepository categoriesRepository;

    @After
    public void cleanup() {
        categoriesRepository.deleteAll();
    }

    @Test
    public void Categories_Service_save_Test() {
        //given
        String categoryName = "categoryName";

        CategoriesSaveRequestDto categoriesSaveRequestDto = CategoriesSaveRequestDto.builder()
                .categoryName(categoryName)
                .build();

        //when
        Long idx = categoriesService.save(categoriesSaveRequestDto);

        //then
        assertThat(categoriesRepository.findById(idx)).isPresent();
        assertThat(categoriesRepository.findById(idx).get().getCategoryName()).isEqualTo(categoryName);
    }

}
