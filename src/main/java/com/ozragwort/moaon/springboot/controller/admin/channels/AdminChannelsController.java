package com.ozragwort.moaon.springboot.controller.admin.channels;

import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsRequestRequestDto;
import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.admin.AdminSecretKeyDto;
import com.ozragwort.moaon.springboot.dto.apiResult.ApiResult;
import com.ozragwort.moaon.springboot.dto.apiResult.FailedResponse;
import com.ozragwort.moaon.springboot.dto.channels.ChannelsResponseDto;
import com.ozragwort.moaon.springboot.service.channels.ChannelsService;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeChannelsService;
import com.ozragwort.moaon.springboot.util.Email.ChannelRecommendedEvent;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class AdminChannelsController {

    private final ApplicationEventPublisher publisher;
    private final YoutubeChannelsService youtubeChannelsService;
    private final ChannelsService channelsService;

    @PostMapping("/admin/channels/crud")
    public ResponseEntity<ApiResult> adminSaveChannel(@RequestBody AdminChannelsSaveRequestDto requestDto) {
        ChannelsResponseDto responseDto = youtubeChannelsService.save(requestDto);

        ApiResult apiResult;
        apiResult = responseDto == null
                ? new ApiResult().failed(new FailedResponse(400, "Invalid Channel ID : " + requestDto.getChannelId()))
                : new ApiResult().succeed(responseDto);

        return ResponseEntity.ok().body(apiResult);
    }

    @PostMapping("/admin/channels/requests")
    public ResponseEntity<ApiResult> adminRequestsChannel(@RequestBody AdminChannelsRequestRequestDto requestDto) {
        publisher.publishEvent(new ChannelRecommendedEvent(requestDto.getContent()));

        ApiResult apiResult = new ApiResult().succeed(true);
        return ResponseEntity.ok().body(apiResult);
    }

    @PutMapping("/admin/channels/crud/{channelId}")
    public ResponseEntity<ApiResult> adminRefreshChannel(@PathVariable String channelId,
                                                         @RequestBody(required = false) AdminSecretKeyDto secret) {
        ChannelsResponseDto responseDto = youtubeChannelsService.refresh(channelId, secret);

        ApiResult apiResult = new ApiResult().succeed(responseDto);
        return ResponseEntity.ok().body(apiResult);
    }

    @PutMapping("/admin/channels/crud/{channelId}/upload")
    public ResponseEntity<ApiResult> uploadUpdate(@PathVariable String channelId,
                                                  @RequestBody(required = false) AdminSecretKeyDto secret) {
        youtubeChannelsService.uploadUpdate(channelId, secret);

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
        List<ChannelsResponseDto> channels = channelsService.findAll(keyword, pageable);
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
