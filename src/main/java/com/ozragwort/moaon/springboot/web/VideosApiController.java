package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.SearchService;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class VideosApiController {

    private final VideosService videosService;

    private final SearchService searchService;

    @PostMapping("/videos")
    public String save(@RequestBody VideosSaveRequestDto requestDto) {
        return videosService.save(requestDto);
    }

    @PostMapping("/videos/{videoId}/relations")
    public String saveRelations(@PathVariable String videoId, @RequestBody RelatedVideosSaveRequestDto requestDto) {
        return videosService.saveRelations(videoId, requestDto);
    }

    @PutMapping("/videos/{videoId}")
    public String update(@PathVariable String videoId, @RequestBody VideosUpdateRequestDto requestDto) {
        return videosService.update(videoId, requestDto);
    }

    @PutMapping("/videos/{videoId}/relations")
    public String addRelations(@PathVariable String videoId, @RequestBody RelatedVideosUpdateRequestDto requestDto) {
        return videosService.addRelations(videoId, requestDto);
    }

    @GetMapping("/videos/{videoId}")
    public List<VideosResponseDto> findById(@PathVariable String videoId) {
        return videosService.findByVideoId(videoId);
    }

    @GetMapping("/videos/{videoId}/relations")
    public List<RelatedVideosResponseDto> findRelationsByVideoId(@PathVariable String videoId) {
        return videosService.findRelationsByVideoId(videoId);
    }

    @GetMapping("/videos")
    public List<VideosResponseDto> find(
            // 필터링
            @RequestParam(value = "no", required = false) Long idx,
            @RequestParam(value = "id", required = false) String videoId,
            @RequestParam(value = "channel", required = false) String channelId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "randomChannel", required = false) String randomChannelId,
            @RequestParam(value = "randomCategory", required = false) Long randomCategoryId,
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "overAvg", defaultValue = "false") boolean avg,
            @RequestParam(value = "topScore", defaultValue = "0") double score,
            // 조건
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "publishedDateUnderHour", required = false) Long hour,
            @RequestParam(value = "random", defaultValue = "false") boolean random
    ) {
        if (idx != null) {
            return videosService.findById(idx);
        } else if (videoId != null) {
            return videosService.findByVideoId(videoId);
        } else if (channelId != null) {
            return videosService.findByChannelId(channelId, random, sortCheck(size, pageCount, sort));
        } else if (categoryId != null) {
            if (tags != null) {
                return searchService.searchVideosByTags(tags, categoryId, random, sortCheck(size, pageCount, sort));
            } else if(hour != null) {
                return videosService.findByPublishedDate(hour, random, sortCheck(size, pageCount, sort));
            } else if (avg) {
                return videosService.findOverAvgRandByScore(size);
            } else if (score != 0) {
                return videosService.findByScore(score, random, sortCheck(size, pageCount, sort));
            } else if (keyword != null) {
                return searchService.searchVideosByKeywords(keyword, (pageCount - 1) * size, size);
            } else {
                return videosService.findByCategoryIdx(categoryId, random, sortCheck(size, pageCount, sort));
            }
        }
        else {
            return videosService.findAll(sortCheck(size, pageCount, sort));
        }
    }

    @DeleteMapping("/videos/{videoId}")
    public String delete(@PathVariable String videoId) {
        return videosService.delete(videoId);
    }

    @DeleteMapping("/videos/{videoId}/relations")
    public String deleteRelations(@PathVariable String videoId) {
        return videosService.deleteRelations(videoId);
    }

    private Pageable sortCheck(int size, int pageCount, String sort) {
        if (sort == null) {
            return PageRequest.of(pageCount - 1, size);
        } else if (sort.equals("asc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("videoPublishedDate").ascending());
        } else if (sort.equals("desc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("videoPublishedDate").descending());
        } else if (sort.equals("popular")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("viewCount").descending());
        } else if (sort.equals("asc-score")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("score").ascending());
        } else if (sort.equals("desc-score")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("score").descending());
        } else {
            return PageRequest.of(pageCount - 1, size);
        }
    }

}
