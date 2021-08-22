package com.ozragwort.moaon.springboot.v1.controller;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsUpdateRequestDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.channels.ChannelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class V1_ChannelsApiController {

    private final ChannelsService channelsService;

    @GetMapping("/channels/{channelId}/videos-tags")
    public List<String> v1_findTagsByChannelId(
            @PathVariable String channelId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size
    ) {
        List<String> tags = channelsService.findTagsByChannelId(channelId);
        return tags.subList(0, size);
    }

    @GetMapping("/channels")
    public List<ChannelsResponseDto> v1_findAll(
            @RequestParam(value = "id", required = false) String channelId,
            @RequestParam(value = "category", required = false) Long categoryId,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "0") int pageCount,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "subscribers", defaultValue = "0") int subscribers,
            @RequestParam(value = "subscribersOver", defaultValue = "true") boolean subscribersOver,
            @RequestParam(value = "random", defaultValue = "false") boolean random
    ) {
        Map<String, Object> keyword = new HashMap<>();
        Sort s = Sort.unsorted();

        if (channelId != null) {
            keyword.put("CHANNELID", channelId);
        }
        if (categoryId != null) {
            keyword.put("CATEGORYID", categoryId.toString());
        }
        if (subscribers != 0) {
            if (subscribersOver) {
                keyword.put("SUBSCRIBEROVER", subscribers);
            } else {
                keyword.put("SUBSCRIBERUNDER", subscribers);
            }
        }
        if (random) {
            keyword.put("RANDOM", true);
        }
        if (sort != null) {
            if (sort.equals("asc")) {
                s = Sort.by("idx").ascending();
            } else if (sort.equals("desc")) {
                s = Sort.by("idx").descending();
            } else if (sort.equals("popular")) {
                s = Sort.by("subscribers").descending();
            }
        }

        Pageable pageable = PageRequest.of(pageCount, size, s);
        return channelsService.findAll(keyword, pageable);
    }

}
