package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.CategoriesService;
import com.ozragwort.moaon.springboot.web.dto.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.web.dto.CategoriesSaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.CategoriesUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class CategoriesApiController {

    private final CategoriesService categoriesService;

    @PostMapping("/categories")
    public Long save(@RequestBody CategoriesSaveRequestDto requestDto) {
        return categoriesService.save(requestDto);
    }

    @PutMapping("/categories/{idx}")
    public Long update(@PathVariable Long idx, @RequestBody CategoriesUpdateRequestDto requestDto) {
        return categoriesService.update(idx, requestDto);
    }

    @GetMapping("/categories")
    public List<CategoriesResponseDto> findAll(
            @RequestParam(value = "id", required = false) Long idx,
            @RequestParam(value = "name", required = false) String categoryName,
            @RequestParam(value = "maxResult", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount) {
        return categoriesService.findAll();
    }

    @GetMapping("/categories/{idx}")
    public CategoriesResponseDto findById(@PathVariable Long idx) {
        return categoriesService.findById(idx);
    }

    @DeleteMapping("/categories/{idx}")
    public Long delete(@PathVariable Long idx) {
        return categoriesService.delete(idx);
    }

}
