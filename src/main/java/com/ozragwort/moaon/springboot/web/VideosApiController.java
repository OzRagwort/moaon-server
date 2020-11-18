package com.ozragwort.moaon.springboot.web;

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
            @RequestParam(value = "maxResult", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount
    ) {
        if (idx != null)
            return videosService.findById(idx);
        else if (videoId != null)
            return videosService.findByVideoId(videoId);
        else if (channelId != null)
            return videosService.findByChannelId(channelId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        else if (categoryId != null)
            return videosService.findByCategoryIdx(categoryId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        else
            return videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
    }

    @DeleteMapping("/videos/{idx}")
    public Long delete(@PathVariable Long idx) {
        return videosService.delete(idx);
    }

}
