package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.VideosTagsPopularityService;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularityResponseDto;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularitySaveRequestDto;
import com.ozragwort.moaon.springboot.web.dto.VideosTagsPopularityUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class VideosTagsApiController {

    private final VideosTagsPopularityService videosTagsPopularityService;

    @PostMapping("/popular-tags")
    public Long save(@RequestBody VideosTagsPopularitySaveRequestDto requestDto) {
        return videosTagsPopularityService.save(requestDto);
    }

    @PutMapping("/popular-tags/{idx}")
    public Long update(@PathVariable Long idx, @RequestBody VideosTagsPopularityUpdateRequestDto requestDto) {
        return videosTagsPopularityService.update(idx, requestDto);
    }

    @GetMapping("/popular-tags")
    public List<VideosTagsPopularityResponseDto> find(@RequestParam(value = "category", required = false) Long categoryId,
                                                      @RequestParam(value = "tags", required = false) String tags,
                                                      @RequestParam(value = "random", defaultValue = "false") boolean random
    ) {
        if (categoryId != null && tags != null) {
            return videosTagsPopularityService.findTagsPopularityByTagsAndCategoryId(categoryId, tags);
        } else if (categoryId != null) {
            return videosTagsPopularityService.findTagsPopularityByCategoryId(categoryId, random);
        } else if (tags != null) {
            return videosTagsPopularityService.findByTags(tags);
        } else {
            return null;
        }
    }

    @GetMapping("/popular-tags/{idx}")
    public VideosTagsPopularityResponseDto findById(@PathVariable Long idx) {
        return videosTagsPopularityService.findById(idx);
    }

    @DeleteMapping("/popular-tags/{idx}")
    public Long delete(@PathVariable Long idx) {
        videosTagsPopularityService.delete(idx);

        return idx;
    }

}
