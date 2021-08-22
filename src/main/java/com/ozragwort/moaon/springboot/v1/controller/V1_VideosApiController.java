package com.ozragwort.moaon.springboot.v1.controller;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class V1_VideosApiController {

    private final VideosService videosService;

    @GetMapping("/videos")
    public ResponseEntity<ApiResult> v1_findAll(
            @RequestParam(value = "id", required = false) String videoId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "channel", required = false) String channelId,
            @RequestParam(value = "random", defaultValue = "false") boolean random,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "overAvg", defaultValue = "false") boolean avg,
            @RequestParam(value = "publishedDateUnderHour", required = false) Long hour,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int pageCount
            ) {
        Map<String, Object> keyword = new HashMap<>();
        Sort s = Sort.unsorted();

        if (videoId != null) {
            keyword.put("VIDEOID", videoId);
        }
        if (categoryId != null) {
            keyword.put("CATEGORYID", categoryId.toString());
        }
        if (channelId != null) {
            keyword.put("CHANNELID", channelId);
        }
        if (random) {
            keyword.put("RANDOM", true);
        }
        if (search != null) {
            keyword.put("SEARCH", search);
        }
        if (tags != null) {
            keyword.put("TAGS", tags);
        }
        if (avg) {
            keyword.put("SCORE", "80");
        }
        if (sort != null) {
            if (sort.equals("asc")) {
                s = Sort.by("videoPublishedDate").ascending();
            } else if (sort.equals("desc")) {
                s = Sort.by("videoPublishedDate").descending();
            } else if (sort.equals("popular")) {
                s = Sort.by("viewCount").descending();
            } else if (sort.equals("asc-score")) {
                s = Sort.by("score").ascending();
            } else if (sort.equals("desc-score")) {
                s = Sort.by("score").descending();
            }
        }
        if (hour != null) {
            s = Sort.by(Sort.Direction.DESC, "videoPublishedDate");
        }

        Pageable pageable = PageRequest.of(pageCount, size, s);
        List<VideosResponseDto> videosResponseDtoList = videosService.findAll(keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(videosResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

}
