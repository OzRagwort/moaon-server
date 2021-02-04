package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.VideosResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class DebugApiController {

    private final VideosService videosService;

    @GetMapping("/debug/videos")
    public List<VideosResponseDto> find(
            @RequestParam(value = "modifiedSort", required = false) String modifiedSort,
            @RequestParam(value = "maxResults", defaultValue = "10") int size,
            @RequestParam(value = "page", defaultValue = "1") int pageCount
    ) {
        if (modifiedSort.equals("asc")) {
            return videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("modifiedDate").ascending()));
        } else if (modifiedSort.equals("desc")) {
            return videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("modifiedDate").descending()));
        } else {
            return videosService.findAll(PageRequest.of(pageCount - 1, size, Sort.by("idx").descending()));
        }
    }

}
