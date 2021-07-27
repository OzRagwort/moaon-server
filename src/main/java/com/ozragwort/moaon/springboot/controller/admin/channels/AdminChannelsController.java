package com.ozragwort.moaon.springboot.controller.admin.channels;

import com.ozragwort.moaon.springboot.dto.admin.AdminChannelsSaveRequestDto;
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
    public ResponseEntity<ChannelsResponseDto> adminSaveChannel(@RequestBody AdminChannelsSaveRequestDto requestDto) {
        ChannelsResponseDto responseDto = youtubeChannelsService.save(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/admin/channels/crud/{channelId}")
    public ResponseEntity<ChannelsResponseDto> adminRefreshChannel(@PathVariable String channelId) {
        ChannelsResponseDto responseDto = youtubeChannelsService.refresh(channelId);

        return ResponseEntity.ok().body(responseDto);
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
    public ResponseEntity<String> adminDeleteChannel(@PathVariable String channelId) {
        youtubeChannelsService.delete(channelId);

        return ResponseEntity.ok().body(channelId);
    }

}
