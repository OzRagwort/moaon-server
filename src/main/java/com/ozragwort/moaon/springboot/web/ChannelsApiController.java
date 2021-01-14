package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.ChannelsService;
import com.ozragwort.moaon.springboot.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class ChannelsApiController {

    private final ChannelsService channelsService;

    @PostMapping("/channels")
    public Long save(@RequestBody PostChannelsSaveRequestDto requestDto) {
        return channelsService.save(requestDto);
    }

    @PutMapping("/channels/{channelId}")
    public Long update(@PathVariable String channelId) {
        return channelsService.update(channelId);
    }

    @GetMapping("/channels")
    public List<ChannelsResponseDto> find(
            @RequestParam(value = "no", required = false) Long idx,
            @RequestParam(value = "id", required = false) String channelId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "randomCategory", required = false) Long randomCategoryId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount
    ) {
        if (idx != null) {
            return channelsService.findById(idx);
        } else if (channelId != null) {
            return channelsService.findByChannelId(channelId);
        } else if (categoryId != null) {
            return channelsService.findByCategoryIdx(categoryId, PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        } else if (randomCategoryId != null) {
            return channelsService.findByCategoryIdxRand(randomCategoryId, size);
        } else {
            return channelsService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        }
    }

    @DeleteMapping("/channels/{idx}")
    public Long delete(@PathVariable Long idx) {
        return channelsService.delete(idx);
    }

}
