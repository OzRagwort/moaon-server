package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.VideosQualityService;
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
public class VideosQualityApiController {

    private final VideosQualityService videosQualityService;

    @PostMapping("/videos-quality")
    public String save(@RequestBody VideosQualitySaveRequestDto requestDto) {
        return videosQualityService.save(requestDto);
    }

    @PutMapping("/videos-quality/{videoId}")
    public String update(@PathVariable String videoId, @RequestBody VideosQualityUpdateRequestDto requestDto) {
        return videosQualityService.update(videoId, requestDto);
    }

    @GetMapping("/videos-quality/{videoId}")
    public List<VideosQualityResponseDto> findByChannelId(@PathVariable String videoId) {
        return videosQualityService.findByVideoId(videoId);
    }

    @GetMapping("/videos-quality")
    public List<VideosQualityResponseDto> find(
            @RequestParam(value = "no", required = false) Long idx,
            @RequestParam(value = "id", required = false) String videoId,
            @RequestParam(value = "overAvg", defaultValue = "false") boolean avg,
            @RequestParam(value = "topScore", defaultValue = "0") double score,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount,
            @RequestParam(value = "sort", required = false) String sort
            ) {
        if (idx != null) {
            return videosQualityService.findById(idx);
        } else if (videoId != null) {
            return videosQualityService.findByVideoId(videoId);
        } else if (avg) {
            return videosQualityService.findOverAvgRandByScore(size);
        } else if (score != 0) {
            return videosQualityService.findByScore(score, sortCheck(size, pageCount, sort));
        } else {
            return videosQualityService.findAll(sortCheck(size, pageCount, sort));
        }
    }

    @DeleteMapping("/videos-quality/{videoId}")
    public String delete(@PathVariable String videoId) {
        return videosQualityService.delete(videoId);
    }

    private Pageable sortCheck(int size, int pageCount, String sort) {
        if (sort == null) {
            return PageRequest.of(pageCount - 1, size);
        } else if (sort.equals("asc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("score").ascending());
        } else if (sort.equals("desc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("score").descending());
        } else if (sort.equals("popular")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("score").descending());
        } else {
            return PageRequest.of(pageCount - 1, size);
        }
    }

}
