package com.ozragwort.moaon.springboot.controller.videos;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.videos.*;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v2")
public class VideosApiController {

    private final VideosService videosService;

    @PostMapping("/videos")
    public ResponseEntity<ApiResult> save(@RequestBody VideosSaveRequestDto requestDto) {
        VideosResponseDto videosSnippetResponseDto = videosService.save(requestDto);

        ApiResult apiResult = new ApiResult().succeed(videosSnippetResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @PutMapping("/videos/{videoId}")
    public ResponseEntity<ApiResult> update(@PathVariable String videoId, @RequestBody VideosUpdateRequestDto requestDto) {
        VideosResponseDto videosSnippetResponseDto = videosService.update(videoId, requestDto);

        ApiResult apiResult = new ApiResult().succeed(videosSnippetResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/videos/{videoId}")
    public ResponseEntity<ApiResult> findById(@PathVariable String videoId) {
        VideosSnippetResponseDto videosSnippetResponseDto = videosService.findByVideoId(videoId);

        ApiResult apiResult = new ApiResult().succeed(videosSnippetResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/videos/{videoId}/statistics")
    public ResponseEntity<ApiResult> findStatisticsById(@PathVariable String videoId) {
        VideosStatisticsResponseDto videosStatisticsResponseDto = videosService.findStatisticsById(videoId);

        ApiResult apiResult = new ApiResult().succeed(videosStatisticsResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/videos")
    public ResponseEntity<ApiResult> findAll(
            @RequestParam(required = false) Map<String, Object> keyword,
            @PageableDefault(sort = "videoPublishedDate", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        List<VideosSnippetResponseDto> videosSnippetResponseDtoList = videosService.findAll(keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(videosSnippetResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @DeleteMapping("/videos/{videoId}")
    public ResponseEntity<ApiResult> delete(@PathVariable String videoId) {
        videosService.delete(videoId);

        ApiResult apiResult = new ApiResult().succeed(true);
        return ResponseEntity.ok()
                .body(apiResult);
    }

}
