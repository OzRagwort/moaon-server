package com.ozragwort.moaon.springboot.web;

import com.ozragwort.moaon.springboot.service.VideosService;
import com.ozragwort.moaon.springboot.web.dto.PostChannelUploadsListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v1")
public class VideosUploadsListApiController {

    private final VideosService videosService;

    @PostMapping("/uploads-list")
    public List<Long> save(@RequestBody PostChannelUploadsListDto requestDto) {
        return videosService.saveUploadsListVideos(requestDto);
    }

}
