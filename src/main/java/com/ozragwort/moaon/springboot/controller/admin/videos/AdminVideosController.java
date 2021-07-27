package com.ozragwort.moaon.springboot.controller.admin.videos;

import com.ozragwort.moaon.springboot.dto.admin.AdminVideosSaveRequestDto;
import com.ozragwort.moaon.springboot.dto.videos.VideosResponseDto;
import com.ozragwort.moaon.springboot.service.youtube.YoutubeVideosService;
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
public class AdminVideosController {

    private final YoutubeVideosService youtubeVideosService;

    @PostMapping("/admin/videos/crud")
    public ResponseEntity<VideosResponseDto> adminSaveVideo(@RequestBody AdminVideosSaveRequestDto requestDto) {
        VideosResponseDto responseDto = youtubeVideosService.save(requestDto);

        return ResponseEntity.ok().body(responseDto);
    }

    @PutMapping("/admin/videos/crud/{videoId}")
    public ResponseEntity<VideosResponseDto> adminRefreshVideo(@PathVariable String videoId) {
        VideosResponseDto responseDto = youtubeVideosService.refresh(videoId);

        return ResponseEntity.ok().body(responseDto);
    }

    @GetMapping("/admin/videos/crud")
    public String adminVideoCrud() {
        return "admin/videoCRUD";
    }

    @GetMapping("/admin/videos/list")
    public String adminFindVideo(Model model,
                                 @RequestParam(required = false) Map<String, Object> keyword,
                                 Pageable pageable) {
        List<VideosResponseDto> videosResponseDtoList = youtubeVideosService.findAll(keyword, pageable);
        model.addAttribute("videoList", videosResponseDtoList);

        return "admin/videoList";
    }

    @DeleteMapping("/admin/videos/crud/{videoId}")
    public ResponseEntity<String> adminDeleteVideo(@PathVariable String videoId) {
        youtubeVideosService.delete(videoId);

        return ResponseEntity.ok().body(videoId);
    }

}
