package com.ozragwort.moaon.springboot.controller.categories;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.categories.CategoriesResponseDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.categories.CategoriesService;
import com.ozragwort.moaon.springboot.service.channels.ChannelsService;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v2")
public class CategoriesApiController {

    private final CategoriesService categoriesService;
    private final ChannelsService channelsService;
    private final VideosService videosService;

    @GetMapping("/categories/{idx}")
    public ResponseEntity<ApiResult> findById(@PathVariable Long idx) {
        CategoriesResponseDto categoriesResponseDto = categoriesService.findById(idx);

        ApiResult apiResult = new ApiResult().succeed(categoriesResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/categories/{idx}/channels")
    public ResponseEntity<ApiResult> findChannelsById(
            @PathVariable Long idx,
            @RequestParam(required = false) Map<String, Object> keyword,
            Pageable pageable
    ) {
        List<ChannelsResponseDto> channelsResponseDtoList = channelsService.findAllByCategoriesId(idx, keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(channelsResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/categories/{idx}/tags")
    public ResponseEntity<ApiResult> findTagsById(@PathVariable Long idx, Pageable pageable) {
        List<String> tags = videosService.findTagsByCategoryId(idx, pageable);

        ApiResult apiResult = new ApiResult().succeed(tags);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/categories/{idx}/videos")
    public ResponseEntity<ApiResult> findVideosById(
            @PathVariable Long idx,
            @RequestParam(required = false) Map<String, Object> keyword,
            Pageable pageable
    ) {
        List<VideosResponseDto> videosResponseDtoList = videosService.findAllByCategoriesId(idx, keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(videosResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResult> findAll() {
        List<CategoriesResponseDto> categoriesResponseDtoList = categoriesService.findAll();

        ApiResult apiResult = new ApiResult().succeed(categoriesResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

}
