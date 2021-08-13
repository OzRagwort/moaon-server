package com.ozragwort.moaon.springboot.controller.admin.channels;

import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.apiResult.FailedResponse;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeChannelsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class AdminChannelsController {

    private final YoutubeChannelsService youtubeChannelsService;

    @PostMapping("/admin/channels/crud")
    public ResponseEntity<ApiResult> adminSaveChannel(@RequestBody AdminChannelsSaveRequestDto requestDto) {
        ChannelsResponseDto responseDto = youtubeChannelsService.save(requestDto);

        ApiResult apiResult;
        apiResult = responseDto == null
                ? new ApiResult().failed(new FailedResponse(400, "Invalid Channel ID : " + requestDto.getChannelId()))
                : new ApiResult().succeed(responseDto);

        return ResponseEntity.ok().body(apiResult);
    }

    @PutMapping("/admin/channels/crud/{channelId}")
    public ResponseEntity<ApiResult> adminRefreshChannel(@PathVariable String channelId) {
        ChannelsResponseDto responseDto = youtubeChannelsService.refresh(channelId);

        ApiResult apiResult = new ApiResult().succeed(responseDto);
        return ResponseEntity.ok().body(apiResult);
    }

    @PutMapping("/admin/channels/crud/{channelId}/upload")
    public ResponseEntity<ApiResult> uploadUpdate(@PathVariable String channelId) {
        youtubeChannelsService.uploadUpdate(channelId);

        ApiResult apiResult = new ApiResult().succeed(true);
        return ResponseEntity.ok()
                .body(apiResult);
    }

    @GetMapping("/admin/channels/crud")
    public String adminChannelCrud() {
        return "admin/channelCRUD";
    }

    @GetMapping("/admin/channels/list")
    public String adminFindChannel(Model model,
                                   @RequestParam(required = false) Map<String, Object> keyword,
                                   Pageable pageable) {
        List<ChannelsResponseDto> channels = youtubeChannelsService.findAll(keyword, pageable);
        model.addAttribute("channelList", channels);

        return "admin/channelList";
    }

    @DeleteMapping("/admin/channels/crud/{channelId}")
    public ResponseEntity<ApiResult> adminDeleteChannel(@PathVariable String channelId) {
        youtubeChannelsService.delete(channelId);

        ApiResult apiResult = new ApiResult().succeed(channelId);
        return ResponseEntity.ok().body(apiResult);
    }

}
