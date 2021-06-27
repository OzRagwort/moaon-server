package com.ozragwort.moaon.springboot.v1.web;

import com.ozragwort.moaon.springboot.v1.service.YoutubeService;
import com.ozragwort.moaon.springboot.v1.web.dto.YoutubeChannelUploadsListRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.YoutubeChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.v1.web.dto.YoutubeVideosSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/moaon/v1")
public class YoutubeApiController {

    private final YoutubeService youtubeService;

    @PostMapping("/yt-channels")
    public @ResponseBody String createChannelByYoutube(@RequestBody YoutubeChannelsSaveRequestDto requestDto) {
        return youtubeService.updateChannelByYoutube(requestDto);
    }

    @PostMapping("/yt-videos")
    public @ResponseBody String createVideoByYoutube(@RequestBody YoutubeVideosSaveRequestDto requestDto) {
        return youtubeService.updateVideoByYoutube(requestDto);
    }

    @PostMapping("/yt-upload")
    public @ResponseBody List<Long> createVideosByUploadsList(@RequestBody YoutubeChannelUploadsListRequestDto requestDto) {
        return youtubeService.updateVideosByUploadsList(requestDto);
    }

}
