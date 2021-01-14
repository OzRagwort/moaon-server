package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.SearchService;
import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    public Long save(@RequestBody PostVideosRequestDto requestDto) {
        return videosService.save(requestDto);
    }

    @PostMapping("/videos/uploadslist")
    public List<Long> saveUploadsListVideos(@RequestBody PostChannelUploadsListDto uploadsListDto) {
        return videosService.saveUploadsListVideos(uploadsListDto);
    }

    @PutMapping("/videos/{videoId}")
    public Long update(@PathVariable String videoId) {
        return videosService.update(videoId);
    }

    @GetMapping("/videos")
    public List<VideosResponseDto> find(
            @RequestParam(value = "no", required = false) Long idx,
            @RequestParam(value = "id", required = false) String videoId,
            @RequestParam(value = "channel", required = false) String channelId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "randomChannel", required = false) String randomChannelId,
            @RequestParam(value = "randomCategory", required = false) Long randomCategoryId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "search", required = false) String keyword
    ) {
        if (idx != null) {
            return videosService.findById(idx);
        } else if (videoId != null) {
            return videosService.findByVideoId(videoId);
        } else if (channelId != null) {
            if (sort == null) {
                return videosService.findByChannelId(channelId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
            } else if (sort.equals("asc")) {
                return videosService.findByChannelIdSort(channelId, PageRequest.of(pageCount - 1, size, Sort.by("videoPublishedDate").ascending()));
            } else if (sort.equals("desc")) {
                return videosService.findByChannelIdSort(channelId, PageRequest.of(pageCount - 1, size, Sort.by("videoPublishedDate").descending()));
            } else if (sort.equals("popular")) {
                return videosService.findByChannelIdSort(channelId, PageRequest.of(pageCount - 1, size, Sort.by("viewCount").descending()));
            } else {
                return videosService.findByChannelId(channelId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
            }
        } else if (categoryId != null) {
            return videosService.findByCategoryIdx(categoryId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        } else if (randomChannelId != null) {
            return videosService.findByChannelIdRand(randomChannelId, size);
        } else if (randomCategoryId != null) {
            return videosService.findByCategoryIdxRand(randomCategoryId, size);
        } else if (keyword != null) {
            return searchService.searchVideos(keyword, (pageCount - 1) * size, size);
        }
        else {
            return videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        }
    }

    @DeleteMapping("/videos/{idx}")
    public Long delete(@PathVariable Long idx) {
        return videosService.delete(idx);
    }

}
