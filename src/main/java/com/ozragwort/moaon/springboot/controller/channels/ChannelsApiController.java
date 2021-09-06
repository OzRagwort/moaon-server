package com.ozragwort.moaon.springboot.controller.channels;

import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsUpdateRequestDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.channels.ChannelsService;
import com.ozragwort.moaon.springboot.service.videos.VideosService;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeChannelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/moaon/v2")
public class ChannelsApiController {

    private final ChannelsService channelsService;
    private final VideosService videosService;
    private final YoutubeChannelsService youtubeChannelsService;

    @PostMapping("/channels")
    public ResponseEntity<ApiResult> save(@RequestBody ChannelsSaveRequestDto requestDto) {
        ChannelsResponseDto channelsResponseDto = channelsService.save(requestDto);

        ApiResult apiResult = new ApiResult().succeed(channelsResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @PutMapping("/channels/{channelId}")
    public ResponseEntity<ApiResult> update(@PathVariable String channelId, @RequestBody ChannelsUpdateRequestDto requestDto) {
        ChannelsResponseDto channelsResponseDto = channelsService.update(channelId, requestDto);

        ApiResult apiResult = new ApiResult().succeed(channelsResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/channels/{channelId}")
    public ResponseEntity<ApiResult> findById(@PathVariable String channelId) {
        ChannelsResponseDto channelsResponseDto = channelsService.findByChannelId(channelId);
        if (channelsResponseDto != null) {
            channelsResponseDto = youtubeChannelsService.refresh(channelsResponseDto.getChannelId());
        }

        ApiResult apiResult = new ApiResult().succeed(channelsResponseDto);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/channels/{channelId}/tags")
    public ResponseEntity<ApiResult> findTagsByChannelId(@PathVariable String channelId) {
        List<String> tags = channelsService.findTagsByChannelId(channelId);

        ApiResult apiResult = new ApiResult().succeed(tags);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/channels/{channelId}/videos")
    public ResponseEntity<ApiResult> findVideosByChannelId(
            @PathVariable String channelId,
            @RequestParam(required = false) Map<String, Object> keyword,
            Pageable pageable
    ) {
        youtubeChannelsService.refresh(channelId);
        List<VideosResponseDto> videosResponseDtoList =
                videosService.findAllByChannelsId(channelId, keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(videosResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/channels")
    public ResponseEntity<ApiResult> findAll(
            @RequestParam(required = false) Map<String, Object> keyword,
            Pageable pageable
    ) {
        List<ChannelsResponseDto> channelsResponseDtoList = channelsService.findAll(keyword, pageable);

        ApiResult apiResult = new ApiResult().succeed(channelsResponseDtoList);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @DeleteMapping("/channels/{channelId}")
    public ResponseEntity<ApiResult> delete(@PathVariable String channelId) {
        channelsService.delete(channelId);

        ApiResult apiResult = new ApiResult().succeed(true);
        return ResponseEntity.ok()
                .body(apiResult);
    }

}
