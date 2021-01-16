package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.RelatedVideosService;
import com.ozragwort.moaon.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class RelatedVideosApiController {

    private final RelatedVideosService relatedVideosService;

    @PostMapping("/relation-videos")
    public List<Long> save(@RequestBody PostRelatedVideosSaveRequestDto requestDto) {
        return relatedVideosService.save(requestDto);
    }

    @PutMapping("/relation-videos/{idx}")
    public Long update(@PathVariable Long idx, @RequestBody RelatedVideosUpdateRequestDto requestDto) {
        return relatedVideosService.update(idx, requestDto);
    }

    @GetMapping("/relation-videos")
    public List<RelatedVideosResponseDto> find(
            @RequestParam(value = "id", required = false) String videoId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount
    ) {
        if (videoId != null)
            return relatedVideosService.findByVideoId(videoId);
        else
            return relatedVideosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
    }

    @GetMapping("/relation-videos/{idx}")
    public List<RelatedVideosResponseDto> findById(@PathVariable Long idx) {
        return relatedVideosService.findById(idx);
    }

    @DeleteMapping("/relation-videos/{idx}")
    public Long delete(@PathVariable Long idx) {
        return relatedVideosService.delete(idx);
    }

}
