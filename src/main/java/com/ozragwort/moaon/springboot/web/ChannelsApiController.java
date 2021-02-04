package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.ChannelsService;
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
public class ChannelsApiController {

    private final ChannelsService channelsService;

    private final VideosQualityService videosQualityService;

    @PostMapping("/channels")
    public String save(@RequestBody ChannelsSaveRequestDto requestDto) {
        return channelsService.save(requestDto);
    }

    @PutMapping("/channels/{channelId}")
    public String update(@PathVariable String channelId, @RequestBody ChannelsUpdateRequestDto requestDto) {
        return channelsService.update(channelId, requestDto);
    }

    @GetMapping("/channels/{channelId}")
    public List<ChannelsResponseDto> findByChannelId(@PathVariable String channelId) {
        return channelsService.findByChannelId(channelId);
    }

    @GetMapping("/channels/{channelId}/videos-quality")
    public double findQualityByChannelId(@PathVariable String channelId) {
        return videosQualityService.getScoreAvgByChannelId(channelId);
    }

    @GetMapping("/channels")
    public List<ChannelsResponseDto> find(
            @RequestParam(value = "no", required = false) Long idx,
            @RequestParam(value = "id", required = false) String channelId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "randomCategory", required = false) Long randomCategoryId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "subscribers", defaultValue = "0") int subscribers,
            @RequestParam(value = "subscribersOver", defaultValue = "true") boolean subscribersOver
    ) {
        if (idx != null) {
            return channelsService.findById(idx);
        } else if (channelId != null) {
            return channelsService.findByChannelId(channelId);
        } else if (categoryId != null) {
            if (subscribers != 0) {
                return channelsService.findBySubscribers(subscribers, subscribersOver, categoryId);
            } else {
                return channelsService.findByCategoryIdx(categoryId, sortCheck(size, pageCount, sort));
            }
        } else if (randomCategoryId != null) {
            return channelsService.findByCategoryIdxRand(randomCategoryId, size);
        } else {
            return channelsService.findAll(sortCheck(size, pageCount, sort));
        }
    }

    @DeleteMapping("/channels/{channelId}")
    public String delete(@PathVariable String channelId) {
        return channelsService.delete(channelId);
    }

    private Pageable sortCheck(int size, int pageCount, String sort) {
        if (sort == null) {
            return PageRequest.of(pageCount - 1, size);
        } else if (sort.equals("asc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("idx").ascending());
        } else if (sort.equals("desc")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("idx").descending());
        } else if (sort.equals("popular")) {
            return PageRequest.of(pageCount - 1, size, Sort.by("subscribers").descending());
        } else {
            return PageRequest.of(pageCount - 1, size);
        }
    }

}
